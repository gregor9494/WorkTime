package ggaworowski.worktime.screens.history

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.MvpVSView
import ggaworowski.worktime.model.DayHoursItemSelectionModel
import ggaworowski.worktime.model.MonthModel
import ggaworowski.worktime.screens.history.item_model.DayHoursItem
import ggaworowski.worktime.screens.history.item_model.MonthItem
import io.reactivex.Observable

/**
 * Created by GG on 03.03.2018.
 */
interface HistoryView : MvpVSView<HistoryViewState> {
    val onCenterMonthChanged: Observable<MonthItem>
    fun showDayDetails(dayHoursItem: DayHoursItemSelectionModel?)
    val paymentClicked: Observable<Any>
    fun showCalculatePayment(monthModel: MonthModel)
}