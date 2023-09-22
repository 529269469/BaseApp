package com.example.psychology.base_app.view.viewpage;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * @author Created by yingjie.zhao on 2021/5/20 0020.
 * Desc:
 */
class ViewPageHelper {
    ViewPager viewPager;

    ViewPagerScroll scroller;

    public ViewPageHelper(ViewPager viewPager) {
        this.viewPager = viewPager;
        init();
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public ViewPagerScroll getScroller() {
        return scroller;
    }


    public void setCurrentItem(int item, boolean somoth) {
        int current = viewPager.getCurrentItem();
        //如果页面相隔大于1,就设置页面切换的动画的时间为0
        if (Math.abs(current - item) > 1) {
            scroller.setNoDuration(true);
            viewPager.setCurrentItem(item, somoth);
            scroller.setNoDuration(false);
        } else {
            scroller.setNoDuration(false);
            viewPager.setCurrentItem(item, somoth);
        }
    }

    private void init() {
        scroller = new ViewPagerScroll(viewPager.getContext());
        Class<ViewPager> cl = ViewPager.class;
        try {
            Field field = cl.getDeclaredField("mScroller");
            field.setAccessible(true);
            //利用反射设置mScroller域为自己定义的MScroller
            field.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
