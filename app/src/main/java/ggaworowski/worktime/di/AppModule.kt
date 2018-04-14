package ggaworowski.justkotlinapplication.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ggaworowski.worktime.receiver.WifiComponent
import ggaworowski.worktime.screens.day_details.DayDetailsComponent
import ggaworowski.worktime.screens.history.HistoryComponent
import ggaworowski.worktime.screens.main.MainComponent
import ggaworowski.worktime.screens.payment_calculator.CalculatePaymentComponent
import javax.inject.Singleton

@Module(subcomponents = arrayOf(
        HistoryComponent::class,
        WifiComponent::class,
        CalculatePaymentComponent::class,
        DayDetailsComponent::class,
        MainComponent::class
))
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

}
