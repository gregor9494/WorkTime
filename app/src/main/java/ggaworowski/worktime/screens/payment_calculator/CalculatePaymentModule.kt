package ggaworowski.worktime.screens.payment_calculator

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import dagger.Module
import dagger.Provides

@Module
class CalculatePaymentModule {

    @Provides
    fun provideCalculatePaymentView(calculatePaymentActivity: CalculatePaymentActivity): CalculatePaymentView {
        return calculatePaymentActivity
    }

    @Provides
    fun provideCalculatePaymentPresenter(ipComunicator: IPComunicator): CalculatePaymentPresenter {
        return CalculatePaymentPresenter(ipComunicator)
    }

}
