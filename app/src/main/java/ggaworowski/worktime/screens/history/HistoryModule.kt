package ggaworowski.worktime.screens.history

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import dagger.Module
import dagger.Provides
import ggaworowski.worktime.data.repositories.WorkTimeRepository

@Module
class HistoryModule {

    @Provides
    fun provideHistoryView(addLessonActivity: HistoryActivity): HistoryView {
        return addLessonActivity
    }

    @Provides
    fun provideHistoryPresenter(workTimeRepository: WorkTimeRepository, ipComunicator: IPComunicator): HistoryPresenter {
        return HistoryPresenter(workTimeRepository, ipComunicator)
    }

}
