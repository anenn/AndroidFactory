package com.anenn.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.anenn.slidingmenu.DragLayout.Status;

public class MyRelativeLayout extends RelativeLayout {
    private DragLayout dragLayout;

    public MyRelativeLayout(Context context) {
        this(context, null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
    }

    public void setDragLayout(DragLayout dl) {
        this.dragLayout = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dragLayout.getStatus() != Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragLayout.getStatus() != Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dragLayout.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
