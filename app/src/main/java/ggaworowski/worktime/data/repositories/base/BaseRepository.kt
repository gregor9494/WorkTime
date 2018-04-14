package ggaworowski.worktime.data.repositories.base

import android.util.Log
import ggaworowski.worktime.utils.RxRealm
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery

/**
 * Created by GG on 03.03.2018.
 */
open class BaseRepository<T : RealmObject>(var classOfModel: Class<T>) {

    val rxRealm: RxRealm<T> = RxRealm(classOfModel)

    fun getAllObs(): Observable<MutableList<T>> {
        return rxRealm.listenList { realm -> realm.where(classOfModel).findAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
    }

    fun getAllCustomObs(requery: (RealmQuery<T>) -> (RealmQuery<T>)): Observable<MutableList<T>> {
        return rxRealm.listenList { realm -> requery(realm.where(classOfModel)).findAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
    }

    fun getAllCustom(requery: (RealmQuery<T>) -> (RealmQuery<T>)): Observable<MutableList<T>> {
        return rxRealm.getList { realm -> requery(realm.where(classOfModel)).findAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
    }

    fun getAll(): Observable<MutableList<T>> {
        return rxRealm.getList { realm -> realm.where(classOfModel).findAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
    }

    open fun insertOrUpdate(model: T): Observable<T> {
        return rxRealm.doTransactional { realm -> realm.insertOrUpdate(model) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .andThen(Observable.just(model))
    }

}