package ggaworowski.worktime.screens.main

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import dagger.Module
import dagger.Provides
import ggaworowski.worktime.data.repositories.SettingsRepository
import ggaworowski.worktime.data.repositories.WorkTimeRepository

@Module
class MainModule {

    @Provides
    fun provideHistoryView(addLessonActivity: MainActivity): MainView {
        return addLessonActivity
    }

    @Provides
    fun provideHistoryPresenter(settingsRepository: SettingsRepository,
                                workTimeRepository: WorkTimeRepository,
                                ipComunicator: IPComunicator): MainPresenter {
        return MainPresenter(settingsRepository,workTimeRepository, ipComunicator)
    }

}
