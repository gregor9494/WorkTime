package ggaworowski.worktime.screens.day_details

import ggaworowski.worktime.model.SingleWorkDuration
import ggaworowski.worktime.screens.day_details.item_model.WorkDurationModel
import ggaworowski.worktime.utils.DateParser

/**
 * Created by gregor on 24.03.18.
 */
class DayDetailsViewCreator {
    val dateParser = DateParser()

    fun convertToDataList(it: List<SingleWorkDuration>): MutableList<WorkDurationModel> {
        val list = mutableListOf<WorkDurationModel>()
        it.forEach({
            list.add(WorkDurationModel(it, dateParser))
        })
        return list
    }
}