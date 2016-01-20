package com.anenn.core.view;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 文本输入框之文本内容监听器
 * Created by Youdar on 2016/1/20.
 */
public class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}