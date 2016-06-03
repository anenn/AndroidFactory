package com.anenn.core.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by anenn on 4/6/16.
 */
public class MyProgressDialog extends ProgressDialog {

    public MyProgressDialog(Context context) {
        this(context, 0);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);

        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public final void showLoadingDialog() {
        showLoadingDialog("Loading...");
    }

    public final void showLoadingDialog(@NonNull String message) {
        setMessage(message);
        show();
    }

    public final void dismissDialog() {
        dismiss();
    }
}
