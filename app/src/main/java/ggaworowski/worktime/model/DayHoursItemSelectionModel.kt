package ggaworowski.worktime.model

import android.widget.RelativeLayout
import android.widget.TextView
import ggaworowski.worktime.screens.history.item_model.DayHoursItem

/**
 * Created by gregor on 14.04.18.
 */
class DayHoursItemSelectionModel(var tvDate: TextView? = null,
                                 var tvWeekDay: TextView? = null,
                                 var tvHoursCount: TextView? = null,
                                 var container: RelativeLayout?=null,
                                 var dayHoursItem: DayHoursItem)