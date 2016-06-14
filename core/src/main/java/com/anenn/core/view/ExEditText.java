package com.anenn.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
    private Drawable clearDrawable;
    // 可见的密码图标资源
    private Drawable openDrawable;
    // 不可见的密码图标资源
    private Drawable closeDrawable;

    // 当前 EditText 是否支持一键清除数据
    private boolean isClearDataEnable;

    public ExEditText(Context context) {
        this(context, null);
    }

    public ExEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs, defStyleAttr);
        initState();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExEditText, defStyleAttr, 0);
        final int attrCount = typedArray.getIndexCount();
        for (int i = 0; i < attrCount; i++) {
            final int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ExEditText_clear_drawable) {
                clearDrawable = typedArray.getDrawable(attr);
                isClearDataEnable = true;
            } else if (attr == R.styleable.ExEditText_open_drawable) {
                openDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.ExEditText_close_drawable) {
                clearDrawable = typedArray.getDrawable(attr);
            }
        }

        typedArray.recycle();
    }

    private void initState() {
        if (clearDrawable != null)
            clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        if (openDrawable != null)
            openDrawable.setBounds(0, 0, openDrawable.getIntrinsicWidth(), openDrawable.getIntrinsicHeight());
        if (closeDrawable != null)
            closeDrawable.setBounds(0, 0, closeDrawable.getIntrinsicWidth(), closeDrawable.getIntrinsicHeight());

        // 只有当前 EditText 为可清除数据的状态才设置监听器
        if (isClearDataEnable) {
            addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    displayDrawable(s.length() > 0);
                }
            });
        } else {
            // 默认密码不可见
            setRightDrawable(closeDrawable);
        }
    }

    /**
     * true 表示显示右边清除按钮图标
     *
     * @param visible true表示可见，反之亦然
     */
    private void displayDrawable(boolean visible) {
        if (visible) {
            setRightDrawable(clearDrawable);
        } else {
            setRightDrawable(null);
        }
    }

    /**
     * 设置控件右侧按钮图标
     *
     * @param drawable 图标资源
     */
    private void setRightDrawable(Drawable drawable) {
        setCompoundDrawables(null, null, drawable, null);
    }

    public void setClearDrawable(Drawable clearDrawable) {
        this.clearDrawable = clearDrawable;
    }

    public void setOpenDrawable(Drawable openDrawable) {
        this.openDrawable = openDrawable;
        setRightDrawable(openDrawable);
    }

    public void setCloseDrawable(Drawable closeDrawable) {
        this.closeDrawable = closeDrawable;
        setRightDrawable(closeDrawable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    dealTouchEvent();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void dealTouchEvent() {
        if (isClearDataEnable) {
            this.setText("");
        } else {
            if (getCompoundDrawables()[2] == closeDrawable) {
                setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                setRightDrawable(openDrawable);
            } else {
                setTransformationMethod(PasswordTransformationMethod.getInstance());
                setRightDrawable(closeDrawable);
            }
        }
    }
}