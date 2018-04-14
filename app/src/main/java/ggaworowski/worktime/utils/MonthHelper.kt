package ggaworowski.worktime.utils

import ggaworowski.worktime.R

/**
 * Created by gregor on 24.03.18.
 */
class MonthHelper {
    companion object {
        @JvmStatic fun getMonthStringRes(monthIndex: Int) = when (monthIndex) {
            0 -> R.string.january
            1 -> R.string.february
            2 -> R.string.march
            3 -> R.string.april
            4 -> R.string.may
            5 -> R.string.june
            6 -> R.string.july
            7 -> R.string.august
            8 -> R.string.september
            9 -> R.string.october
            10 -> R.string.november
            11 -> R.string.december
            else -> R.string.january
        }
    }

}