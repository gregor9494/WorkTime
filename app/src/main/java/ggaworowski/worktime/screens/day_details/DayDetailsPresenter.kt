package ggaworowski.worktime.screens.day_details

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseVSPresenter
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by gregor on 24.03.18.
 */
class DayDetailsPresenter(ipComunicator: IPComunicator, var workTimeRepository: WorkTimeRepository) : BaseVSPresenter<DayDetailsView, DayDetailsViewState>(ipComunicator) {

    var day: Date? = null
    val dayDetailsViewCreator = DayDetailsViewCreator()

    override fun createViewState(): DayDetailsViewState = DayDetailsViewState()

    override fun attachView(view: DayDetailsView) {
        super.attachView(view)
        loadData()
    }

    fun loadData() {
        subscribeViewState(
                workTimeRepository.getWorkHistoryForDay(day ?: Date())
                        .subscribeOn(Schedulers.io())
                        .map {
                            viewState.dailyWorkModel = it
                            viewState.loading = false
                            viewState.durationsItems = dayDetailsViewCreator.convertToDataList(it.list)
                            viewState
                        }
                        .observeOn(AndroidSchedulers.mainThread())
        )
    }


}