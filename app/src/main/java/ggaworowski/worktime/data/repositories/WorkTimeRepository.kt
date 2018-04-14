package ggaworowski.worktime.data.repositories

import ggaworowski.worktime.data.repositories.base.BaseRepository
import ggaworowski.worktime.model.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.Sort
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by GG on 03.03.2018.
 */
class WorkTimeRepository() : BaseRepository<WorkEventModel>(WorkEventModel::class.java) {
    fun getActualStateObservable(): Observable<ActualWorkState> {

        return getAllCustomObs {
            val calendar = getTodayMidnight()
            val beginDate = Date(calendar.time.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val endDate = Date(calendar.time.time)

            it.sort("date", Sort.ASCENDING)
                    .between("date", beginDate, endDate)
        }
                .map {
                    calculateActualWorkState(it)
                }
    }

    fun getPossibleMonths(): Observable<ArrayList<MonthModel>> {
        return Observable.fromCallable({
            Realm.getDefaultInstance().use {
                val list = ArrayList<MonthModel>()

                val maxDate = it.where(WorkEventModel::class.java).maximumDate("date")
                val minDate = it.where(WorkEventModel::class.java).minimumDate("date")
                val cal = Calendar.getInstance()
                cal.time = minDate

                while (cal.time.before(maxDate)) {
                    list.add(MonthModel(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)))
                    cal.add(Calendar.MONTH, 1)
                }

                val now = Calendar.getInstance()
                val thisMonth = MonthModel(now.get(Calendar.MONTH), now.get(Calendar.YEAR))
                if (!list.contains(thisMonth)) {
                    list.add(thisMonth)
                }


                return@fromCallable list
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getActualWorkState(): Observable<ActualWorkState> {

        return getAllCustom {
            val calendar = getTodayMidnight()
            val beginDate = Date(calendar.time.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val endDate = Date(calendar.time.time)

            it.sort("date", Sort.ASCENDING)
                    .between("date", beginDate, endDate)
        }
                .map {
                    calculateActualWorkState(it)
                }
    }

    fun getWorkHistoryForDay(date: Date): Observable<DailyWorkModel> {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val bTime = calendar.time.time
        val beginDate = Date(bTime)

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val eTime = calendar.time.time
        val endDate = Date(eTime)


        return getAllCustom {
            it.sort("date", Sort.ASCENDING)
                    .between("date", beginDate, endDate)
        }
                .map { it.toList().toMutableList() }
                .map {
                    parseToDailyWorkModel(date, it, beginDate, endDate)
                }
    }

    private fun parseToDailyWorkModel(date: Date, it: MutableList<WorkEventModel>, beginDate: Date, endDate: Date): DailyWorkModel {
        val dailyWorkModel = DailyWorkModel()
        dailyWorkModel.date = date
        val duration = calculateDuration(it)
        dailyWorkModel.duration = duration
        if (it.size > 0 && !it[0].workOn) {
            it.add(0, WorkEventModel(true, beginDate))
        }
        if (it.size > 0 && it[it.size - 1].workOn) {
            it.add(WorkEventModel(false, endDate))
        }

        val list = mutableListOf<SingleWorkDuration>()
        it.forEach({
            if (it.workOn && (list.size == 0 || (list.size > 0 && list[list.size - 1].dateEnd != null))) {
                list.add(SingleWorkDuration(dateStart = it.date))
            } else if (list.size > 0) {
                list[list.size - 1].dateEnd = it.date
            }
        })
        dailyWorkModel.list = list

        return dailyWorkModel
    }

    fun getWorkHistoryForMonth(date: Date): Single<List<DailyWorkHoursModel>> {
        return getAllCustom {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.time = date
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val bTime = calendar.time.time
            val beginDate = Date(bTime)

            calendar.add(Calendar.MONTH, 1)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))

            val eTime = calendar.time.time
            val endDate = Date(eTime)

            it.sort("date", Sort.ASCENDING)
                    .between("date", beginDate, endDate)
        }
                .flatMapIterable { it }
                .collectInto(mutableMapOf<HistoryGroupKey, MutableList<WorkEventModel>>()) { map, workEventModel ->
                    if (map.contains(workEventModel.getHistoryGroupKey())) map[workEventModel.getHistoryGroupKey()]?.add(workEventModel)
                    else map.put(workEventModel.getHistoryGroupKey(), mutableListOf(workEventModel))
                }
                .map { it.toList().toMutableList() }
                .map {
                    val list = mutableListOf<DailyWorkHoursModel>()
                    it.forEach({
                        val duration = calculateDuration(it.second)
                        list.add(DailyWorkHoursModel(it.first.toDate(), duration))
                    })
                    list
                }
    }

    private fun getTodayMidnight(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    private fun calculateDuration(it: MutableList<WorkEventModel>): Long {
        val workEventModel = ActualWorkState()

        if (it.size == 0) {
            workEventModel.duration = 0
            workEventModel.isWorkOn = false
        } else {
            workEventModel.isWorkOn = it[it.size - 1].workOn
            var duration = 0L
            var lastWorkEventModel: WorkEventModel? = null
            if (workEventModel.isWorkOn) {
                it.add(WorkEventModel(false, Date()))
            }
            it.forEach({
                if (it.workOn) {
                    lastWorkEventModel = it
                } else {
                    val timeNow = (it.date?.time ?: 0)
                    val lastTime = (lastWorkEventModel?.date?.time ?: 0)
                    if (timeNow != 0L && lastTime != 0L)
                        duration += timeNow - lastTime
                }
            })

            workEventModel.duration = duration
        }

        return workEventModel.duration
    }

    private fun calculateActualWorkState(it: MutableList<WorkEventModel>): ActualWorkState {
        val workEventModel = ActualWorkState()

        if (it.size == 0) {
            workEventModel.duration = 0
            workEventModel.isWorkOn = false
        } else {
            workEventModel.isWorkOn = it[it.size - 1].workOn
            var duration = 0L
            var lastWorkEventModel: WorkEventModel? = null
            if (workEventModel.isWorkOn) {
                it.add(WorkEventModel(false, Date()))
            }
            it.forEach({
                if (it.workOn) {
                    lastWorkEventModel = it
                } else {
                    val timeNow = (it.date?.time ?: 0)
                    val lastTime = (lastWorkEventModel?.date?.time ?: 0)
                    if (timeNow != 0L && lastTime != 0L)
                        duration += timeNow - lastTime
                }
            })

            workEventModel.duration = duration
        }

        return workEventModel
    }

    override fun insertOrUpdate(model: WorkEventModel): Observable<WorkEventModel> {
        return rxRealm.doTransactional { realm ->
            run {
                val maxDate = realm.where(WorkEventModel::class.java).maximumDate("date")
                val first = realm.where(WorkEventModel::class.java)
                        .equalTo("date", maxDate)
                        .findFirst()

                val canAdd = first == null || (first.workOn != model.workOn && first.date?.before(model.date) == true)

                if (canAdd) {
                    realm.insertOrUpdate(model)
                }
            }
        }
                .andThen(Observable.just(model))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}