package ggaworowski.worktime.utils;

/**
 * Created by gregor on 17.03.18.
 */

import android.os.Build;
import android.os.HandlerThread;
import android.os.Process;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RxRealm<T extends RealmObject> {

    private Class<T> classOfModel;
    private Realm customRealm;

    public RxRealm(Class<T> classOfModel) {
        this.classOfModel = classOfModel;
    }

    public Flowable<List<T>> listenList(
            final Function<Realm, RealmResults<T>> query) {
        final AtomicReference<Realm> realmReference = new AtomicReference<>(null);
        return listenRealmResults(query, realmReference)
                .map(result -> realmReference.get().copyFromRealm(result));
    }

    public Flowable<T> listenElement(
            final Function<Realm, RealmResults<T>> query) {
        final AtomicReference<Realm> realmReference = new AtomicReference<>(null);
        return listenRealmResults(query, realmReference)
                .filter(result -> !result.isEmpty())
                .map(result -> realmReference.get().copyFromRealm(result.first()));
    }

    public Maybe<List<T>> getList(
            final Function<Realm, RealmResults<T>> query) {
        return Maybe.create(emitter -> {
            final Realm realm = getRealm();
            final RealmResults<T> result = query.apply(realm);
            if (result != null && result.isLoaded() && result.isValid()) {
                emitter.onSuccess(realm.copyFromRealm(result));
            } else {
                emitter.onComplete();
            }
            emitter.setCancellable(realm::close);
        });
    }

    public Maybe<T> getElement(final Function<Realm, T> query) {
        return Maybe.create(emitter -> {
            final Realm realm = getRealm();
            final T result = query.apply(realm);
            if (result != null && result.isLoaded() && result.isValid()) {
                emitter.onSuccess(realm.copyFromRealm(result));
            } else {
                emitter.onComplete();
            }
            emitter.setCancellable(realm::close);
        });
    }

    public Completable doTransactional(final Consumer<Realm> transaction) {
        return Completable.fromAction(() -> {
            try (Realm realm = getRealm()) {
                realm.executeTransaction(realm1 -> {
                    try {
                        transaction.accept(realm1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private <T extends RealmObject> Flowable<RealmResults<T>> listenRealmResults(
            final Function<Realm, RealmResults<T>> query, AtomicReference<Realm> realmReference) {
        final HandlerThread dbHandler = createDbHandler();
        final Scheduler scheduler = AndroidSchedulers.from(dbHandler.getLooper());
        return Flowable.<RealmResults<T>>create(emitter -> {
            final Realm realm = getRealm();
            realmReference.set(realm);
            final RealmChangeListener<RealmResults<T>> listener = result -> {
                if (emitter.isCancelled() || !result.isLoaded() || !result.isValid()) return;
                emitter.onNext(result);
            };
            final RealmResults<T> result = query.apply(realm);
            if (!emitter.isCancelled() && result.isLoaded() && result.isValid()) {
                emitter.onNext(result);
            }
            result.addChangeListener(listener);
            emitter.setCancellable(() -> {
                result.removeChangeListener(listener);
                realm.close();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    dbHandler.quitSafely();
                } else {
                    dbHandler.quit();
                }
            });
        }, BackpressureStrategy.LATEST)
                .subscribeOn(scheduler)
                .unsubscribeOn(scheduler);
    }

    private HandlerThread createDbHandler() {
        final HandlerThread handlerThread = new HandlerThread("RealmReadThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        return handlerThread;
    }

    private Realm getRealm() {
        return customRealm != null ? customRealm : Realm.getDefaultInstance();
    }

    public void setCustomRealm(Realm customRealm) {
        this.customRealm = customRealm;
    }
}
