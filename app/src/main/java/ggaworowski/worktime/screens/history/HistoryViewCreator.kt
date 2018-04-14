package ggaworowski.worktime.screens.history

import ggaworowski.worktime.model.DailyWorkHoursModel
import ggaworowski.worktime.model.DayHoursItemSelectionModel
import ggaworowski.worktime.model.MonthModel
import ggaworowski.worktime.screens.history.item_model.DayHoursItem
import ggaworowski.worktime.screens.history.item_model.MonthItem
import ggaworowski.worktime.utils.DateParser
import io.reactivex.subjects.PublishSubject

/**
 * Created by gregor on 22.03.18.
 */
class HistoryViewCreator {
    var dateParser = DateParser()

    fun convertToDataList(it: List<DailyWorkHoursModel>, daySelected: PublishSubject<DayHoursItemSelectionModel>): MutableList<DayHoursItem> {
        val list = mutableListOf<DayHoursItem>()
        it.forEach({
            list.add(DayHoursItem(it, dateParser,daySelected))
        })
        return list
    }

    fun convertToMonthList(it: ArrayList<MonthModel>, selectedItem: MonthItem,
                           itemSelectionObservable : PublishSubject<MonthItem>): MutableList<MonthItem> {
        val list = mutableListOf<MonthItem>()
        it.forEach({
            val selected = it.theSameMonth(selectedItem.monthModel)
            list.add(MonthItem(it,selected,itemSelectionObservable))
        })
        return list
    }
}