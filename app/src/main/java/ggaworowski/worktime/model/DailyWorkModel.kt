package ggaworowski.worktime.model

import java.util.*

/**
 * Created by gregor on 24.03.18.
 */
class DailyWorkModel(var duration: Long = 0,
                     var list: MutableList<SingleWorkDuration> = mutableListOf(),
                     var date: Date? = null) {
}