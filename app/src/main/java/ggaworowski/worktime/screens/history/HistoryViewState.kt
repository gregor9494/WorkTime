package ggaworowski.worktime.screens.history

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.ViewState
import ggaworowski.worktime.model.MonthModel
import ggaworowski.worktime.screens.history.item_model.DayHoursItem
import ggaworowski.worktime.screens.history.item_model.MonthItem

/**
 * Created by GG on 03.03.2018.
 */
class HistoryViewState(var loading: Boolean,
                       var data: List<DayHoursItem>,
                       var centerMonthItem: MonthItem? = null,
                       var monthItems: List<MonthItem> = listOf(MonthItem(MonthModel.getDefaultTodayModel(), itemSelectionObservable = null)),
                       var selectedMonth: MonthItem = monthItems[0]) : ViewState() {
    fun refreshMonthSelection() {
        monthItems.forEach {
            it.selected = it.monthModel.theSameMonth(selectedMonth.monthModel)
        }
    }
}