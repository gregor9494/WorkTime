package ggaworowski.worktime.model

/**
 * Created by GG on 03.03.2018.
 */
class ActualWorkState {
    var isWorkOn = false
    var duration : Long = 0

    override fun toString(): String {
        return "ActualWorkState(isWorkOn=$isWorkOn, duration=$duration)"
    }
}