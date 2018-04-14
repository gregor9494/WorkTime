package ggaworowski.worktime.screens.payment_calculator

import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = arrayOf(CalculatePaymentModule::class))
interface CalculatePaymentComponent : AndroidInjector<CalculatePaymentActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<CalculatePaymentActivity>()
}
