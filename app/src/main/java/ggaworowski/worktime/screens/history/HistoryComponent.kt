package ggaworowski.worktime.screens.history

import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = arrayOf(HistoryModule::class))
interface HistoryComponent : AndroidInjector<HistoryActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HistoryActivity>()
}
