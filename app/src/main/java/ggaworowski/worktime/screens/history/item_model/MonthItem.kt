package ggaworowski.worktime.screens.history.item_model

import android.content.res.ColorStateList
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import ggaworowski.worktime.R
import ggaworowski.worktime.model.MonthModel
import ggaworowski.worktime.utils.MonthHelper
import io.reactivex.subjects.PublishSubject

/**
 * Created by GG on 11.03.2018.
 */
class MonthItem(val monthModel: MonthModel, var selected: Boolean = false,
                val itemSelectionObservable: PublishSubject<MonthItem>? = null) : AbstractFlexibleItem<MonthItem.MyViewHolder>() {

    override fun getLayoutRes(): Int = R.layout.month_item

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: MyViewHolder?, position: Int, payloads: MutableList<Any>?) {
        holder?.bind(monthModel, this, itemSelectionObservable)
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): MyViewHolder {
        return MyViewHolder(view, adapter)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonthItem
        if (monthModel != other.monthModel) return false

        return true
    }

    inner class MyViewHolder(view: View?, adapter: FlexibleAdapter<*>?) : FlexibleViewHolder(view, adapter) {
        var tvMonth: TextView? = view?.findViewById(R.id.tvMonth)

        fun bind(monthModel: MonthModel, monthItem: MonthItem, itemSelectionObservable: PublishSubject<MonthItem>?) {
            tvMonth?.clicks()?.subscribe({
                itemSelectionObservable?.onNext(monthItem)
            }, { it.printStackTrace() })
            tvMonth?.text = tvMonth?.context?.getString(MonthHelper.getMonthStringRes(monthModel.month))
            val textColorRes = if (monthItem.selected) R.color.white else R.color.grey
            val color = tvMonth?.context?.resources?.getColor(textColorRes) ?: 0
            tvMonth?.setTextColor(color)
        }
    }
}