package com.anenn.core.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Anenn on 15-7-23.
 */
public class L {
    private static final String TAG = "com.anenn.core";
    public static boolean DEBUG = false;

    public static void setDebugEnable(boolean debugEnable) {
        DEBUG = debugEnable;
    }

    public static void d(String argc) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(argc)) {
                Log.d(TAG, argc);
            }
        }
    }

    public static void i(String argc) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(argc)) {
                Log.i(TAG, argc);
            }
        }
    }

    public static void w(String argc) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(argc)) {
                Log.w(TAG, argc);
            }
        }
    }

    public static void e(String argc) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(argc)) {
                Log.e(TAG, argc);
            }
        }
    }
}
