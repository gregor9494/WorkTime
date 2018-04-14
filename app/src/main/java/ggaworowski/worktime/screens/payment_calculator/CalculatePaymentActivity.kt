package ggaworowski.worktime.screens.payment_calculator

import com.jakewharton.rxbinding2.view.RxView
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseActivity
import ggaworowski.worktime.R
import ggaworowski.worktime.model.MonthModel
import kotlinx.android.synthetic.main.calculate_payment_activity.*
import javax.inject.Inject

class CalculatePaymentActivity : BaseActivity<CalculatePaymentView, CalculatePaymentPresenter>(), CalculatePaymentView {

    companion object {
        val MONTH_MODEL_EXTRA = "month_model"
    }

    @Inject
    lateinit var calculatePaymentPresenter: CalculatePaymentPresenter

    override val calculateClicked by lazy {
        RxView.clicks(ivCalculate)
    }

    override fun getLayout(): Int = R.layout.calculate_payment_activity

    override fun setupViews() {
    }

    override fun createPresenter(): CalculatePaymentPresenter {
        val monthModel = intent.getSerializableExtra(MONTH_MODEL_EXTRA) as MonthModel
        calculatePaymentPresenter.monthModel = monthModel
        return calculatePaymentPresenter
    }

    override fun render(vs: CalculatePaymentViewState?) {

    }
}
