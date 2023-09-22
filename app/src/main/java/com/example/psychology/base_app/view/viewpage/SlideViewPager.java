package com.example.psychology.base_app.view.viewpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class SlideViewPager extends ViewPager {
    private boolean mIsSlide = true;//是否可以滑动
    private ViewPageHelper helper;

    public SlideViewPager(@NonNull Context context) {
        super(context);
    }

    public SlideViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        helper = new ViewPageHelper(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsSlide;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIsSlide;
    }

    public void toggleSlide(boolean isSlide) {
        this.mIsSlide = isSlide;
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        ViewPagerScroll scroller = helper.getScroller();
        if (Math.abs(getCurrentItem() - item) > 1) {
            scroller.setNoDuration(true);
            super.setCurrentItem(item, smoothScroll);
            scroller.setNoDuration(false);
        } else {
            scroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }
}
