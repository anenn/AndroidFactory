package com.anenn.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by Anenn on 15-7-23.
 */
public class WeakHandler extends Handler {
    private WeakReference<Activity> reference;
    private Callback callback;

    public WeakHandler(Activity activity, Callback callback) {
        reference = new WeakReference<>(activity);
        this.callback = callback;
    }

    public WeakHandler(Fragment fragment, Callback callback) {
        reference = new WeakReference<Activity>(fragment.getActivity());
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Activity activity = reference.get();
        if (activity != null && callback != null) {
            callback.handleMessage(msg);
        }
    }
}
