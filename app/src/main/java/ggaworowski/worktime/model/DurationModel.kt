package ggaworowski.worktime.model

import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by GG on 11.03.2018.
 */
data class DurationModel(val days: Int, val hours: Int, val minutes: Int) {
    fun dailyDuration(): String {
        val formattedHours = String.format("%02d", hours)
        val formattedMinutes = String.format("%02d", minutes)
        return "$formattedHours:$formattedMinutes"
    }

    fun hours() :String{
        val formattedHours = String.format("%02d", hours)
        return formattedHours
    }

    fun minutes() : String{
        val formattedMinutes = String.format("%02d", minutes)
        return formattedMinutes
    }
}