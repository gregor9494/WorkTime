package ggaworowski.worktime.model

import java.util.*

/**
 * Created by GG on 11.03.2018.
 */
class HistoryGroupKey(val day : Int,val month : Int, val year : Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryGroupKey

        if (day != other.day) return false
        if (month != other.month) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = month
        result = 31 * result + year
        return result
    }

    fun toDate() : Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH,day)
        cal.set(Calendar.MONTH,month)
        cal.set(Calendar.YEAR,year)
        return cal.time
    }
}