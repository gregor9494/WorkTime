package ggaworowski.worktime.screens.day_details

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import dagger.Module
import dagger.Provides
import ggaworowski.worktime.data.repositories.WorkTimeRepository

@Module
class DayDetailsModule {

    @Provides
    fun provideCalculatePaymentView(calculatePaymentActivity: DayDetailsActivity): DayDetailsView {
        return calculatePaymentActivity
    }

    @Provides
    fun provideCalculatePaymentPresenter(ipComunicator: IPComunicator, workTimeRepository: WorkTimeRepository): DayDetailsPresenter {
        return DayDetailsPresenter(ipComunicator, workTimeRepository)
    }

}
