package ggaworowski.worktime.screens.day_details

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import ggaworowski.worktime.R
import ggaworowski.worktime.utils.DateParser
import ggaworowski.worktime.utils.DurationHelper
import kotlinx.android.synthetic.main.day_details_activity.*
import java.util.*
import javax.inject.Inject

class DayDetailsActivity : BaseActivity<DayDetailsView, DayDetailsPresenter>(), DayDetailsView {
    companion object {
        val DATE_KEY = "date"
    }

    @Inject
    lateinit var dayDetailsPresenter: DayDetailsPresenter

    var durationAdapter = FlexibleAdapter(emptyList())
    val dateParser = DateParser()

    override fun getLayout(): Int = R.layout.day_details_activity

    override fun createPresenter(): DayDetailsPresenter {
        dayDetailsPresenter.day = intent.getSerializableExtra(DATE_KEY) as Date?
        return dayDetailsPresenter
    }

    override fun setupViews() {
        setupDurationsList()
    }

    private fun setupDurationsList() {
        rvTimeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTimeList.adapter = durationAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        tvDate.visibility = View.GONE
        tvWeekDay.visibility = View.GONE
        tvHoursCount.visibility = View.GONE
        rvTimeList.visibility=View.GONE
    }

    override fun render(vs: DayDetailsViewState?) {
        durationAdapter.updateDataSet(vs?.durationsItems)
        tvHoursCount.text = DurationHelper.createDurationModel(vs?.dailyWorkModel?.duration
                ?: 0).dailyDuration()
        tvWeekDay.text = dateParser.getDayName(vs?.dailyWorkModel?.date)
        tvDate.text = dateParser.getDateText(vs?.dailyWorkModel?.date)
    }

}
