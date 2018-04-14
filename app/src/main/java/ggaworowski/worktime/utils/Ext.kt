package ggaworowski.worktime.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by gregor on 24.03.18.
 */
fun RecyclerView.getMiddleItemIndex(): Int {
    if (layoutManager is LinearLayoutManager) {
        val lm = layoutManager as LinearLayoutManager
        return (lm.findFirstVisibleItemPosition() + lm.findLastVisibleItemPosition()) / 2
    }
    return 0
}