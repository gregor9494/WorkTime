package ggaworowski.worktime.utils

import ggaworowski.worktime.model.DurationModel

/**
 * Created by GG on 11.03.2018.
 */
class DurationHelper {
    companion object {
        val DAY = (24 * 60 * 60 * 1000)
        val HOUR = (60 * 60 * 1000)
        val MINUTE = (60 * 1000)

        @JvmStatic
        fun createDurationModel(duration: Long): DurationModel {
            val days = duration / DAY
            val hours = (duration - (days * DAY)) / HOUR
            val minutes = (duration - (hours * HOUR) - (days * DAY)) / MINUTE
            return DurationModel(days.toInt(), hours.toInt(), minutes.toInt())
        }
    }
}