package ggaworowski.worktime.screens.day_details.item_model

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import ggaworowski.worktime.R
import ggaworowski.worktime.model.SingleWorkDuration
import ggaworowski.worktime.utils.DateParser

/**
 * Created by GG on 11.03.2018.
 */
class WorkDurationModel(val singleWorkDuration: SingleWorkDuration, val dateParser: DateParser) : AbstractFlexibleItem<WorkDurationModel.MyViewHolder>() {

    override fun getLayoutRes(): Int = R.layout.work_duration_item

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: MyViewHolder?, position: Int, payloads: MutableList<Any>?) {
        holder?.bind(singleWorkDuration, dateParser)
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): MyViewHolder {
        return MyViewHolder(view, adapter)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as WorkDurationModel
        if (singleWorkDuration != other.singleWorkDuration) return false

        return true
    }

    inner class MyViewHolder(view: View?, adapter: FlexibleAdapter<*>?) : FlexibleViewHolder(view, adapter) {
        var tvDuration: TextView? = view?.findViewById(R.id.tvDuration)

        fun bind(singleWorkDuration: SingleWorkDuration, dateParser: DateParser) {
            val startTime = dateParser.getHourWithMn(singleWorkDuration.dateStart)
            val endTime = dateParser.getHourWithMn(singleWorkDuration.dateEnd)
            tvDuration?.text = "$startTime - $endTime"
        }
    }
}