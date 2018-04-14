package ggaworowski.worktime.screens.main

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseVSPresenter
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import ggaworowski.worktime.data.repositories.SettingsRepository
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import ggaworowski.worktime.model.WorkEventModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * Created by GG on 03.03.2018.
 */
class MainPresenter(var settingsRepository: SettingsRepository, var workTimeRepository: WorkTimeRepository, ipComunicator: IPComunicator) :
        BaseVSPresenter<MainView, MainViewState>(ipComunicator) {

    override fun createViewState(): MainViewState = MainViewState()

    override fun attachView(view: MainView) {
        super.attachView(view)
        loadData()
        setupListeners()
    }

    private fun setupListeners() {
        listenLoadData()
        listenReload()
        listenShowStatsClick()
        listenWifiClick()
        listenWifiEntered()
        listenWifiNameChanged()
        listenWifiCancel()
    }

    private fun listenWifiCancel() {
        ifViewAttached {
            subscribeViewState(
                    it.wifiCancel
                            .flatMap { settingsRepository.setWifiSettings("", false) }
                            .map {
                                viewState.loading = false
                                viewState.showWifiInput = false
                                viewState.wifiOptionOn = it.countWithWifi
                                viewState.wifiName = ""
                                viewState
                            }
            )
        }
    }

    private fun listenWifiNameChanged() {
        ifViewAttached {
            addSubscription(it.wifiNameChanged
                    .subscribe({
                        viewState.wifiName = it
                    }, { it.printStackTrace() }))
        }
    }

    private fun listenWifiEntered() {
        ifViewAttached {
            subscribeViewState(
                    it.wifiEntered
                            .flatMap { settingsRepository.setWifiSettings(it, true) }
                            .map {
                                viewState.loading = false
                                viewState.showWifiInput = false
                                viewState.wifiOptionOn = it.countWithWifi
                                viewState.wifiName = it.wifiSSID
                                viewState
                            }
            )
        }
    }

    private fun listenWifiClick() {
        ifViewAttached {
            subscribeViewState(
                    it.wifiOptionClick
                            .map {
                                viewState.showWifiInput = !viewState.showWifiInput
                                viewState
                            }
            )
        }
    }

    private fun listenShowStatsClick() {
        ifViewAttached {
            it.showHistoryClick
                    .subscribe({
                        ifViewAttached {
                            it.showHistory()
                        }
                    }, { it.printStackTrace() })
        }
    }

    fun listenLoadData() {
        ifViewAttached {
            subscribeViewState(
                    it.startStopClick
                            .flatMap { workTimeRepository.getActualWorkState().subscribeOn(Schedulers.io()) }
                            .flatMap { workTimeRepository.insertOrUpdate(WorkEventModel(!it.isWorkOn, Date())) }
                            .flatMap { workTimeRepository.getActualWorkState().subscribeOn(Schedulers.io()) }
                            .map {
                                viewState.loading = false
                                viewState.data = it
                                viewState
                            }
                            .observeOn(AndroidSchedulers.mainThread()))
        }
    }

    fun listenReload() {
        ifViewAttached {
            subscribeViewState(it.reloadDataIntent
                    .subscribeOn(Schedulers.io())
                    .flatMap { workTimeRepository.getActualWorkState().subscribeOn(Schedulers.io()) }
                    .map {
                        viewState.data = it
                        viewState.loading = false
                        viewState
                    }
                    .observeOn(AndroidSchedulers.mainThread()))
        }
    }

    fun loadData() {
        ifViewAttached {
            subscribeViewState(
                    workTimeRepository.getActualStateObservable()
                            .map {
                                viewState.data = it
                                viewState.loading = false
                                viewState
                            }
                            .flatMap {
                                settingsRepository.getWifiSettings()
                                        .map {
                                            viewState.wifiOptionOn = it.countWithWifi
                                            viewState.wifiName = it.wifiSSID
                                            viewState
                                        }
                            }
                            .observeOn(AndroidSchedulers.mainThread()))
        }
    }


}