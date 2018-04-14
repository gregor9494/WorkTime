package ggaworowski.worktime.screens.history

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseVSPresenter
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import ggaworowski.worktime.model.DayHoursItemSelectionModel
import ggaworowski.worktime.screens.history.item_model.DayHoursItem
import ggaworowski.worktime.screens.history.item_model.MonthItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*


/**
 * Created by GG on 03.03.2018.
 */
class HistoryPresenter(var workTimeRepository: WorkTimeRepository, ipComunicator: IPComunicator) :
        BaseVSPresenter<HistoryView, HistoryViewState>(ipComunicator) {

    val historyViewCreator = HistoryViewCreator()
    val monthSelected = PublishSubject.create<MonthItem>()
    val daySelected = PublishSubject.create<DayHoursItemSelectionModel>()

    override fun createViewState(): HistoryViewState = HistoryViewState(false, emptyList())

    override fun attachView(view: HistoryView) {
        super.attachView(view)
        loadMonth(viewState.selectedMonth.monthModel.toDate())
        loadMonthList()
        listenMonthScroll()
        listenMonthSelection()
        listenDayClick()
        listenPaymentClick()
    }

    private fun listenPaymentClick() {
        ifViewAttached {
            it.paymentClicked.subscribe({
                ifViewAttached {
                    it.showCalculatePayment(viewState.selectedMonth.monthModel)
                }
            }, { it.printStackTrace() })
        }
    }


    private fun listenDayClick() {
        addSubscription(daySelected.subscribe({
            val dayHoursItem = it
            ifViewAttached {
                it.showDayDetails(dayHoursItem)
            }
        }, { it.printStackTrace() }))
    }

    private fun listenMonthSelection() {
        subscribeViewState(
                monthSelected
                        .map {
                            viewState.selectedMonth = it
                            viewState.refreshMonthSelection()
                            viewState
                        }.switchMap {
                            workTimeRepository.getWorkHistoryForMonth(viewState.selectedMonth.monthModel.toDate())
                                    .subscribeOn(Schedulers.io())
                                    .map { historyViewCreator.convertToDataList(it, daySelected) }
                                    .map {
                                        viewState.data = it
                                        viewState
                                    }
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .toObservable()
                        }
        )
    }

    private fun listenMonthScroll() {
        ifViewAttached {
            subscribeViewState(
                    it.onCenterMonthChanged
                            .map {
                                viewState.centerMonthItem = it
                                viewState
                            }
            )
        }
    }

    private fun loadMonthList() {
        ifViewAttached {
            subscribeViewState(
                    workTimeRepository.getPossibleMonths()
                            .subscribeOn(Schedulers.io())
                            .map { historyViewCreator.convertToMonthList(it, viewState.selectedMonth, monthSelected) }
                            .map {
                                viewState.monthItems = it
                                viewState
                            }
                            .observeOn(AndroidSchedulers.mainThread()))
        }
    }

    fun loadMonth(date: Date) {
        ifViewAttached {
            subscribeViewState(
                    workTimeRepository.getWorkHistoryForMonth(date)
                            .subscribeOn(Schedulers.io())
                            .map { historyViewCreator.convertToDataList(it, daySelected) }
                            .map {
                                viewState.data = it
                                viewState
                            }
                            .observeOn(AndroidSchedulers.mainThread()))
        }
    }


}