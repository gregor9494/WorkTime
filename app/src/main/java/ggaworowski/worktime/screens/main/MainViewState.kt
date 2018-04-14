package ggaworowski.worktime.screens.main

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.ViewState
import ggaworowski.worktime.model.ActualWorkState

/**
 * Created by GG on 03.03.2018.
 */

data class MainViewState(var loading: Boolean = false,
                         var wifiOptionOn: Boolean = false,
                         var showWifiInput: Boolean = false,
                         var wifiName: String = "",
                         var data: ActualWorkState? = null) : ViewState()