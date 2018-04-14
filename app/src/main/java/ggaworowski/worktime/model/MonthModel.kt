package ggaworowski.worktime.model

import java.io.Serializable
import java.util.*

/**
 * Created by gregor on 22.03.18.
 */

class MonthModel(var month: Int, var year: Int) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MonthModel

        if (!theSameMonth(other)) return false

        return true
    }

    fun theSameMonth(other: MonthModel): Boolean {
        return this.month == other.month && this.year == other.year
    }

    override fun hashCode(): Int {
        var result = month
        result = 31 * result + year
        return result
    }

    companion object {
        @JvmStatic
        fun getDefaultTodayModel(): MonthModel {
            val cal = Calendar.getInstance()
            return MonthModel(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
        }
    }

    fun toDate(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        return cal.time
    }
}
