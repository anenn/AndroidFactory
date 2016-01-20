package com.anenn.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.anenn.core.R;

/**
 * 增强型文本输入框
 * 主要提供快捷删除文本内容的功能
 * Created by Anenn on 15-7-28.
 */
public class ExEditText extends EditText {

    // 删除按钮图标资源
    private Drawable drawable;

    public ExEditText(Context context) {
        this(context, null);
    }

    public ExEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            initView(context, attrs);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        boolean useCrossed;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExEditText, 0, 0);
        try {
            useCrossed = typedArray.getBoolean(R.styleable.ExEditText_crossed, false);
        } finally {
            if (typedArray != null)
                typedArray.recycle();
        }

        int crossedRes = useCrossed ? R.drawable.core_del_normal : R.drawable.core_del_pressed;
        drawable = ContextCompat.getDrawable(getContext(), crossedRes);
        if (drawable != null)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                displayCrossed(s.length() > 0);
            }
        });
    }

    /**
     * 是否显示删除按钮
     *
     * @param visible true表示可见，反之亦然
     */
    private void displayCrossed(boolean visible) {
        if (visible) {
            setDrawableRight(drawable);
        } else {
            setDrawableRight(null);
        }
    }

    /**
     * 设置图标
     *
     * @param drawable 图标资源
     */
    private void setDrawableRight(Drawable drawable) {
        setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }
}