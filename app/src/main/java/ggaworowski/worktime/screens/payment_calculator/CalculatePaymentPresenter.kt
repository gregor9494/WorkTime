package ggaworowski.worktime.screens.payment_calculator

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseVSPresenter
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import ggaworowski.worktime.model.MonthModel

/**
 * Created by gregor on 24.03.18.
 */
class CalculatePaymentPresenter(ipComunicator: IPComunicator) : BaseVSPresenter<CalculatePaymentView, CalculatePaymentViewState>(ipComunicator) {

    override fun createViewState(): CalculatePaymentViewState = CalculatePaymentViewState()

    lateinit var monthModel: MonthModel

    override fun attachView(view: CalculatePaymentView) {
        super.attachView(view)
        listenCalculateClicked()
    }

    private fun listenCalculateClicked() {
        ifViewAttached {
            it.calculateClicked.subscribe({

            },{})
        }
    }

}