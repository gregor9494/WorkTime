package ggaworowski.worktime.screens.main

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.MvpVSView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by GG on 03.03.2018.
 */
interface MainView : MvpVSView<MainViewState> {
    val startStopClick: Observable<Any>
    val wifiOptionClick: Observable<Any>
    val showHistoryClick: Observable<Any>
    val wifiCancel: Observable<Any>
    val wifiEntered: Observable<String>
    val loadDataIntent: PublishSubject<Any>
    val reloadDataIntent: Observable<Long>
    val wifiNameChanged : Observable<String>
    fun showHistory()
}