package ggaworowski.justkotlinapplication.di

import dagger.Module
import dagger.Provides
import ggaworowski.worktime.data.repositories.SettingsRepository
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideScheduleRepository(): WorkTimeRepository {
        return WorkTimeRepository()
    }


    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepository()
    }
}
