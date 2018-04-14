package ggaworowski.worktime.receiver

import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = arrayOf(WifiModule::class))
interface WifiComponent : AndroidInjector<WifiReceiver> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<WifiReceiver>()
}
