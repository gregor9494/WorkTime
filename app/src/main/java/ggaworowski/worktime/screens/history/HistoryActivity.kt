package ggaworowski.worktime.screens.history

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.scrollChangeEvents
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import ggaworowski.worktime.R
import ggaworowski.worktime.model.DayHoursItemSelectionModel
import ggaworowski.worktime.model.MonthModel
import ggaworowski.worktime.screens.day_details.DayDetailsActivity
import ggaworowski.worktime.screens.history.item_model.DayHoursItem
import ggaworowski.worktime.screens.history.item_model.MonthItem
import ggaworowski.worktime.screens.payment_calculator.CalculatePaymentActivity
import ggaworowski.worktime.utils.getMiddleItemIndex
import kotlinx.android.synthetic.main.activity_history.*
import javax.inject.Inject
import android.support.v4.app.ActivityOptionsCompat


class HistoryActivity : BaseActivity<HistoryView, HistoryPresenter>(), HistoryView {


    @Inject
    lateinit var historyPresenter: HistoryPresenter

    var historyAdapter = FlexibleAdapter(emptyList())
    var monthsAdapter = FlexibleAdapter(emptyList())

    override val onCenterMonthChanged by lazy {
        rvMonths.scrollChangeEvents()
                .map { rvMonths.getMiddleItemIndex() }
                .map { monthsAdapter.getItem(it) as MonthItem }
                .scan({ _, vs2 -> vs2 })
    }

    override fun createPresenter(): HistoryPresenter = historyPresenter!!

    override fun getLayout(): Int = R.layout.activity_history

    override fun setupViews() {
        setupHistoryList()
        setupMonthsList()
    }

    override val paymentClicked by lazy {
        RxView.clicks(fabCalculatePayment)
    }

    private fun setupMonthsList() {
        rvMonths.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMonths.adapter = monthsAdapter
    }

    private fun setupHistoryList() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        } else {
            rvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        rvHistory.adapter = historyAdapter
    }

    override fun render(vs: HistoryViewState?) {
        tvNoWorkedHoursThisMonth.visibility =
                if (vs?.data?.size ?: 0 > 0) View.GONE else View.VISIBLE
        historyAdapter.updateDataSet(vs?.data)
        monthsAdapter.updateDataSet(vs?.monthItems)
        tvYear.text = vs?.centerMonthItem?.monthModel?.year.toString()
    }

    override fun showDayDetails(dayHoursItemSelectionModel: DayHoursItemSelectionModel?) {
        val intent = Intent(this, DayDetailsActivity::class.java)
        intent.putExtra(DayDetailsActivity.DATE_KEY, dayHoursItemSelectionModel?.dayHoursItem?.dailyWorkHoursModel?.date)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
//                android.support.v4.util.Pair(dayHoursItemSelectionModel?.tvDate as View, "date"),
//                android.support.v4.util.Pair(dayHoursItemSelectionModel?.tvHoursCount as View, "hoursCount"),
//                android.support.v4.util.Pair(dayHoursItemSelectionModel?.tvWeekDay as View, "weekDay"),
                android.support.v4.util.Pair(dayHoursItemSelectionModel?.container as View, "container")
        )
        startActivity(intent, options.toBundle())
    }

    override fun showCalculatePayment(monthModel: MonthModel) {
        val intent = Intent(this, CalculatePaymentActivity::class.java)
        intent.putExtra(CalculatePaymentActivity.MONTH_MODEL_EXTRA, monthModel)
        startActivity(intent)
    }

}
