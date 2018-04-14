package ggaworowski.worktime.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import dagger.android.AndroidInjection
import ggaworowski.worktime.data.repositories.SettingsRepository
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import ggaworowski.worktime.model.WorkEventModel
import io.realm.Realm
import java.util.*
import javax.inject.Inject
import ggaworowski.worktime.receiver.WifiReceiver.ConnectivityReceiverListener
import android.net.ConnectivityManager





/**
 * Created by gregor on 14.03.18.
 */

class WifiReceiver(val mConnectivityReceiverListener: ConnectivityReceiverListener? = null) : BroadcastReceiver() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var workTimeRepository: WorkTimeRepository

    override fun onReceive(context: Context, intent: Intent) {
        mConnectivityReceiverListener?.onNetworkConnectionChanged(isConnected(context))
        AndroidInjection.inject(this, context)
        val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (info != null) {

            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid.replace("\"", "")
            settingsRepository
                    .getWifiSettings()
                    .map {
                        Log.e("WifiReceiver","onReceive4")
                        if (it.countWithWifi && it.wifiSSID.toLowerCase() == ssid.toLowerCase()) {
                            val suspended = info.state == NetworkInfo.State.DISCONNECTED || info.state == NetworkInfo.State.SUSPENDED
                            val connected = info.state == NetworkInfo.State.CONNECTED
                            if (suspended) {
                                workTimeRepository.insertOrUpdate(WorkEventModel(false, Date()))
                                        .subscribe({}, { it.printStackTrace() })
                            } else if (connected) {
                                workTimeRepository.insertOrUpdate(WorkEventModel(true, Date()))
                                        .subscribe({}, { it.printStackTrace() })
                            }
                        }
                    }
                    .subscribe({}, { it.printStackTrace() })
        }
    }


    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}
