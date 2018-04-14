package ggaworowski.worktime.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by GG on 11.03.2018.
 */
class DateParser {
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun getDateText(date: Date?): String {
        if (date == null) return ""
        return dateFormat.format(date)
    }

    fun getDayName(date: Date?): String {
        if (date == null) return ""
        return dayFormat.format(date)
    }


    fun getMonthName(date: Date): String {
        return monthFormat.format(date)
    }

    fun getHourWithMn(date: Date?): String {
        if (date == null) return ""
        return hourFormat.format(date)
    }
}