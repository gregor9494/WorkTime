package ggaworowski.worktime.screens.main

import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}
