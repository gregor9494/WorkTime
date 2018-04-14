package ggaworowski.worktime.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by gregor on 17.03.18.
 */

public class HourDots extends TextView {

    private boolean animate = false;

    public HourDots(Context context) {
        super(context);
    }

    public HourDots(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HourDots(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HourDots(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAnimate(boolean b) {
        this.animate = b;
        if (b) {
            startAnimation();
        } else {
            clearAnimation();
            animate().alpha(1f).start();
        }
    }

    public void startAnimation() {
        clearAnimation();
        animate()
                .alpha(getAlpha() == 0 ? 1 : 0)
                .withEndAction(() -> {
                    if (animate) startAnimation();
                })
                .setDuration(500)
                .start();
    }

}
