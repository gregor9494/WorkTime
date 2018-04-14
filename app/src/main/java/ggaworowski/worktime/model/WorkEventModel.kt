package ggaworowski.worktime.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by GG on 03.03.2018.
 */

open class WorkEventModel( var workOn: Boolean = false, var date: Date? = null) : RealmObject() {
    override fun toString(): String {
        return "WorkEventModel(workOn=$workOn, date=$date)"
    }

    fun getHistoryGroupKey(): HistoryGroupKey {
        val cal = Calendar.getInstance()
        cal.time = date
        return HistoryGroupKey(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
    }
}
