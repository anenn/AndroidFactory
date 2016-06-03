package com.anenn.oss;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Anenn on 6/2/16.
 */
public class WeakHandler extends Handler {

    private WeakReference<Callback> mReference;

    public WeakHandler(Callback callback) {
        mReference = new WeakReference<Callback>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if (mReference != null) {
            Callback callback = mReference.get();
            if (callback != null) {
                callback.handleMessage(msg);
            }
        }
    }
}
