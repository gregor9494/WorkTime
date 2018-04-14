package ggaworowski.justkotlinapplication.di

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import ggaworowski.worktime.screens.day_details.DayDetailsActivity
import ggaworowski.worktime.screens.day_details.DayDetailsComponent

import ggaworowski.worktime.screens.history.HistoryActivity
import ggaworowski.worktime.screens.history.HistoryComponent
import ggaworowski.worktime.screens.main.MainActivity
import ggaworowski.worktime.screens.main.MainComponent
import ggaworowski.worktime.screens.payment_calculator.CalculatePaymentActivity
import ggaworowski.worktime.screens.payment_calculator.CalculatePaymentComponent


@Module
abstract class ActivityBuilder {
    @Binds
    @IntoMap
    @ActivityKey(HistoryActivity::class)
    abstract fun bindHistoryActivity(builder: HistoryComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindMainActivity(builder: MainComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(CalculatePaymentActivity::class)
    abstract fun bindCalculatePaymentActivity(builder: CalculatePaymentComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(DayDetailsActivity::class)
    abstract fun bindDayDetailsActivity(builder: DayDetailsComponent.Builder): AndroidInjector.Factory<out Activity>
}