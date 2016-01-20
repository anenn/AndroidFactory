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
    private WeakReference<Activity> reference = null;
    private Callback callback;

    public WeakHandler(Activity activity) {
        reference = new WeakReference<>(activity);
        this.callback = (Callback) activity;
    }

    public WeakHandler(Fragment fragment) {
        reference = new WeakReference<Activity>(fragment.getActivity());
        this.callback = (Callback) fragment;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Activity activity = reference.get();
        if (activity != null) {
            callback.handleMessage(msg);
        }
    }
}
