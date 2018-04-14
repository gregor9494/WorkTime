package ggaworowski.worktime.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by gregor on 14.03.18.
 */
open class SettingsModel(
        @PrimaryKey
        var settingsID: Int = 1,
        var countWithWifi: Boolean = false,
        var wifiSSID: String = "") : RealmObject()