package ggaworowski.worktime.screens.history.item_model

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import ggaworowski.worktime.R
import ggaworowski.worktime.model.DailyWorkHoursModel
import ggaworowski.worktime.model.DayHoursItemSelectionModel
import ggaworowski.worktime.utils.DateParser
import ggaworowski.worktime.utils.DurationHelper
import io.reactivex.subjects.PublishSubject

/**
 * Created by GG on 11.03.2018.
 */
class DayHoursItem(val dailyWorkHoursModel: DailyWorkHoursModel,
                   val dateParser: DateParser,
                   val daySelected: PublishSubject<DayHoursItemSelectionModel>) : AbstractFlexibleItem<DayHoursItem.MyViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: MyViewHolder?, position: Int, payloads: MutableList<Any>?) {
        holder?.bind(this, daySelected)
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): MyViewHolder {
        return MyViewHolder(view, adapter)
    }

    override fun getLayoutRes(): Int = R.layout.day_hours_item


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DayHoursItem
        if (dailyWorkHoursModel != other.dailyWorkHoursModel) return false

        return true
    }

    inner class MyViewHolder(view: View?, adapter: FlexibleAdapter<*>?) : FlexibleViewHolder(view, adapter) {
        var tvDate: TextView?
        var tvWeekDay: TextView?
        var tvHoursCount: TextView?
        var container: RelativeLayout?


        init {
            tvDate = view?.findViewById(R.id.tvDate)
            tvWeekDay = view?.findViewById(R.id.tvWeekDay)
            tvHoursCount = view?.findViewById(R.id.tvHoursCount)
            container = view?.findViewById(R.id.container)
        }

        fun bind(dayHoursItem: DayHoursItem, daySelected: PublishSubject<DayHoursItemSelectionModel>) {
            container?.clicks()?.subscribe({ daySelected.onNext(DayHoursItemSelectionModel(tvDate, tvWeekDay, tvHoursCount, container, dayHoursItem)) }, {})
            tvDate?.text = dateParser.getDateText(dayHoursItem.dailyWorkHoursModel.date)
            tvWeekDay?.text = dateParser.getDayName(dayHoursItem.dailyWorkHoursModel.date)
            tvHoursCount?.text = DurationHelper.createDurationModel(dailyWorkHoursModel.duration).dailyDuration()
        }
    }
}