package ggaworowski.worktime.screens.day_details

import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = arrayOf(DayDetailsModule::class))
interface DayDetailsComponent : AndroidInjector<DayDetailsActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<DayDetailsActivity>()
}
