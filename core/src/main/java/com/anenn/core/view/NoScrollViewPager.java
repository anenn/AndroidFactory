package com.anenn.core.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义非滚动的滑动页容器控件
 * Created by Anenn on 2015/11/27.
 */
public class NoScrollViewPager extends ViewPager {

    // 滑动标志位
    private boolean isCanScroll;

    /**
     * 设置是否可以滚动
     *
     * @param canScroll true表示可以滑动，反之亦然
     */
    public final void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    public NoScrollViewPager(Context context) {
        this(context, null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return isCanScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return isCanScroll && super.onInterceptTouchEvent(arg0);
    }
}