package com.anenn.core;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Anenn on 15-7-23.
 */
public class WeakHandler extends Handler {
    private WeakReference<Callback> callbackReference;

    public WeakHandler(Callback callback) {
        callbackReference = new WeakReference<>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Callback callback = callbackReference.get();
        if (callback != null) {
            callback.handleMessage(msg);
        }
    }
}
