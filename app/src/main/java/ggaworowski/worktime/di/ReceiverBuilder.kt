package ggaworowski.justkotlinapplication.di

import android.app.Activity
import android.content.BroadcastReceiver
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.BroadcastReceiverKey
import dagger.multibindings.IntoMap
import ggaworowski.worktime.receiver.WifiComponent
import ggaworowski.worktime.receiver.WifiReceiver

import ggaworowski.worktime.screens.history.HistoryActivity
import ggaworowski.worktime.screens.history.HistoryComponent
import ggaworowski.worktime.screens.main.MainActivity
import ggaworowski.worktime.screens.main.MainComponent


@Module
abstract class ReceiverBuilder {

    @Binds
    @IntoMap
    @BroadcastReceiverKey(WifiReceiver::class)
    abstract fun bindWifiReceiver(builder: WifiComponent.Builder): AndroidInjector.Factory<out BroadcastReceiver>
}