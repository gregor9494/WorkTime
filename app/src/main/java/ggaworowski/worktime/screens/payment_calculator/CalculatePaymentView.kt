package ggaworowski.worktime.screens.payment_calculator

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.MvpVSView
import io.reactivex.Observable

/**
 * Created by gregor on 24.03.18.
 */
interface CalculatePaymentView : MvpVSView<CalculatePaymentViewState> {
    val calculateClicked: Observable<Any>
}