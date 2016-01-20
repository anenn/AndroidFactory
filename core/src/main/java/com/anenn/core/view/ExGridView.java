package com.anenn.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 增强型GridView，使其高度适配内容
 * Created by Anenn on 2015/11/26.
 */
public class ExGridView extends GridView {

    public ExGridView(Context context) {
        super(context);
    }

    public ExGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
