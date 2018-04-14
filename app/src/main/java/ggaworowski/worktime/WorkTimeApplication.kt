package ggaworowski.worktime

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.support.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import ggaworowski.justkotlinapplication.di.DaggerAppComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject
import android.support.multidex.MultiDex


class WorkTimeApplication : MultiDexApplication(), HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var receiverDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .name("workTime")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> {
        return receiverDispatchingAndroidInjector
    }
}