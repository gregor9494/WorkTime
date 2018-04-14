package ggaworowski.worktime.main_view

import android.support.annotation.NonNull
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import ggaworowski.worktime.data.repositories.SettingsRepository
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import ggaworowski.worktime.model.ActualWorkState
import ggaworowski.worktime.model.SettingsModel
import ggaworowski.worktime.screens.main.MainPresenter
import ggaworowski.worktime.screens.main.MainView
import ggaworowski.worktime.screens.main.MainViewState
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainPresenterTest {

    val workTimeRepository = Mockito.mock(WorkTimeRepository::class.java)
    val settingsRepository = Mockito.mock(SettingsRepository::class.java)
    val ipCommunicator = Mockito.mock(IPComunicator::class.java)
    val mainPresenter = MainPresenter(settingsRepository, workTimeRepository, ipCommunicator)
    val mainView = Mockito.mock(MainView::class.java)

    @Before
    fun init() {
        `when`(mainView.startStopClick).thenReturn(Observable.never())
        `when`(mainView.wifiOptionClick).thenReturn(Observable.never())
        `when`(mainView.showHistoryClick).thenReturn(Observable.never())
        `when`(mainView.wifiCancel).thenReturn(Observable.never())
        `when`(mainView.wifiEntered).thenReturn(Observable.never())
        `when`(mainView.reloadDataIntent).thenReturn(Observable.never())
        `when`(mainView.wifiNameChanged).thenReturn(Observable.never())
        `when`(workTimeRepository.getActualStateObservable()).thenReturn(Observable.just(ActualWorkState()))
        `when`(settingsRepository.getWifiSettings()).thenReturn(Observable.just(SettingsModel(countWithWifi = true, wifiSSID = "wifiName")))
    }

    @Test
    fun testLoadDataWhenWifiOptionIsOn() {
        `when`(workTimeRepository.getActualStateObservable()).thenReturn(Observable.just(ActualWorkState()))
        `when`(settingsRepository.getWifiSettings()).thenReturn(Observable.just(SettingsModel(countWithWifi = true, wifiSSID = "wifiName")))
        mainPresenter.attachView(mainView)
        val captor = ArgumentCaptor.forClass(MainViewState::class.java)
        verify(mainView, Mockito.times(2)).render(captor.capture())

        // First value is for init ViewState
        Assert.assertEquals(captor.allValues[1].wifiName, "wifiName")
        Assert.assertEquals(captor.allValues[1].wifiOptionOn, true)
    }

    @Test
    fun testNewWifiDataEntered() {
        val wifiEnteredPublisher = PublishSubject.create<String>()
        `when`(mainView.wifiEntered).thenReturn(wifiEnteredPublisher)
        `when`(workTimeRepository.getActualStateObservable()).thenReturn(Observable.just(ActualWorkState()))
        `when`(settingsRepository.getWifiSettings()).thenReturn(Observable.just(SettingsModel()))
        `when`(settingsRepository.setWifiSettings("test", true))
                .thenReturn(Observable.just(SettingsModel(1, true, "test")))
        mainPresenter.attachView(mainView)
        wifiEnteredPublisher.onNext("test")

        val captor = ArgumentCaptor.forClass(MainViewState::class.java)
        verify(mainView, Mockito.times(3)).render(captor.capture())

        Assert.assertEquals(captor.allValues[1].wifiName, "test")
        Assert.assertEquals(captor.allValues[1].showWifiInput, false)
    }

    @Test
    fun testViewsStateWifiNameAfterChange() {
        val wifiNamePublisher = PublishSubject.create<String>()
        `when`(mainView.wifiNameChanged).thenReturn(wifiNamePublisher)

        mainPresenter.attachView(mainView)

        wifiNamePublisher.onNext("testName")
        assertEquals(mainPresenter.viewState.wifiName,"testName")

        wifiNamePublisher.onNext("testName2")
        assertEquals(mainPresenter.viewState.wifiName,"testName2")
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
        }
    }
}
