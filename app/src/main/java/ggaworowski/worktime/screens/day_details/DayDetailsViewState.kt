package ggaworowski.worktime.screens.day_details

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.ViewState
import ggaworowski.worktime.model.DailyWorkModel
import ggaworowski.worktime.screens.day_details.item_model.WorkDurationModel

/**
 * Created by gregor on 24.03.18.
 */
class DayDetailsViewState(var dailyWorkModel: DailyWorkModel = DailyWorkModel(),
                          var loading: Boolean = false,
                          var durationsItems: List<WorkDurationModel> = emptyList()) : ViewState()