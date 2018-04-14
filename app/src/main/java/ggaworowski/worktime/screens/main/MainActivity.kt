package ggaworowski.worktime.screens.main

import android.content.Intent
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.textChanges
import com.mosbyextra.ggaworowski.mosbyextralibrary.common.BaseActivity
import ggaworowski.worktime.R
import ggaworowski.worktime.screens.history.HistoryActivity
import ggaworowski.worktime.utils.DurationHelper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {

    companion object {
        val LOG_TAG = "MainActivity"
    }

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun createPresenter(): MainPresenter = mainPresenter

    override fun getLayout(): Int = R.layout.activity_main

    override fun setupViews() {

    }

    override val wifiNameChanged by lazy {
        etWifiName.textChanges().skipInitialValue().map { it.toString() }
    }

    override val wifiCancel by lazy {
        RxView.clicks(ivCancelWifi)
    }

    override val wifiEntered by lazy {
        RxView.clicks(ivConfirmWifi).map { etWifiName?.text?.toString() ?: "" }
    }

    override val startStopClick by lazy {
        RxView.clicks(tvStartStop)
    }

    override val wifiOptionClick by lazy {
        RxView.clicks(llWifiState)
    }

    override val showHistoryClick by lazy {
        RxView.clicks(llShowStats)
    }

    override val loadDataIntent = PublishSubject.create<Any>()

    override val reloadDataIntent = Observable.interval(5, TimeUnit.SECONDS)

    override fun render(mainViewState: MainViewState) {
        Log.e("main:render", mainViewState.toString())

        val durationModel = DurationHelper.createDurationModel(mainViewState.data?.duration ?: 0)
        tvTimeHours.text = durationModel.hours()
        tvTimeMinutes.text = durationModel.minutes()
        tvStartStop.text = if (mainViewState.data?.isWorkOn == true) getString(R.string.end) else getString(R.string.start)
        rlWifiInput.visibility = if (mainViewState.showWifiInput) View.VISIBLE else View.GONE
        tvWifiStatus.text = resources.getString(if (mainViewState.wifiOptionOn) R.string.wifi_on else R.string.wifi_off)
        llWifiContainer.setBackgroundResource(if (mainViewState.wifiOptionOn) R.drawable.wifi_button_background_on else R.drawable.wifi_button_background_off)
        tvTimeDots.setAnimate(mainViewState.data?.isWorkOn == true)
        ivCancelWifi.visibility = if (mainViewState.wifiOptionOn) View.VISIBLE else View.GONE

        if (!etWifiName.text.toString().equals(mainViewState.wifiName)) {
            etWifiName.setText(mainViewState.wifiName)
        }

    }

    override fun showHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

}
