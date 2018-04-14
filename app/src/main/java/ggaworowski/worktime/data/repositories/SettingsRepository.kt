package ggaworowski.worktime.data.repositories

import ggaworowski.worktime.data.repositories.base.BaseRepository
import ggaworowski.worktime.model.*
import io.reactivex.Observable


/**
 * Created by GG on 03.03.2018.
 */
class SettingsRepository() : BaseRepository<SettingsModel>(SettingsModel::class.java) {
    fun setWifiSettings(wifiName: String, b: Boolean): Observable<SettingsModel> {
        return insertOrUpdate(SettingsModel(countWithWifi = b, wifiSSID = wifiName))
    }

    fun getWifiSettings(): Observable<SettingsModel?> {
        return getAll()
                .flatMap {
                    if (it.size > 0) {
                        return@flatMap Observable.just(it[0])
                    } else {
                        return@flatMap insertOrUpdate(SettingsModel(countWithWifi = false, wifiSSID = ""))
                    }
                }
    }
}