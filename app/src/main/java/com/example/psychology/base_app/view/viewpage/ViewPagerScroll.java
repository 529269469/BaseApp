package com.example.psychology.base_app.view.viewpage;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author Created by yingjie.zhao on 2021/5/20 0020.
 * Desc:
 */
class ViewPagerScroll extends Scroller {
    private static final Interpolator sInterpolator = t -> {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    };


    public boolean noDuration;

    public ViewPagerScroll(Context context) {
        this(context, sInterpolator);
    }

    public ViewPagerScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void setNoDuration(boolean noDuration) {
        this.noDuration = noDuration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (noDuration)
            //界面滑动不需要时间间隔
            super.startScroll(startX, startY, dx, dy, 0);
        else
            super.startScroll(startX, startY, dx, dy, duration);
    }
}
