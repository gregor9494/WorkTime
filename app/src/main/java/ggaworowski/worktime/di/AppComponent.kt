package ggaworowski.justkotlinapplication.di

import android.app.Application

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import ggaworowski.worktime.WorkTimeApplication
import ggaworowski.worktime.di.CommunicatorModule

@Singleton
@Component(modules =
arrayOf(AndroidInjectionModule::class,
        AppModule::class,
        RepositoryModule::class,
        CommunicatorModule::class,
        ReceiverBuilder::class,
        ActivityBuilder::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: WorkTimeApplication)

}
