package com.anenn.core.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Anenn on 15-7-23.
 */
public class T {
    private static Toast toast;

    public static void init(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
    }

    public static void show(@NonNull CharSequence message) {
        t(message);
    }

    public static void show(@NonNull String message) {
        t(message);
    }

    public static void showLong(@NonNull CharSequence message) {
        t(message);
        t(message);
    }

    public static void showLong(@NonNull String message) {
        t(message);
        t(message);
    }

    private static void t(@NonNull CharSequence message) {
        if (toast == null) {
            throw new IllegalStateException("Toast is not initialized");
        }

        if (!TextUtils.isEmpty(message)) {
            toast.setText(message);
            toast.show();
        }
    }

    private static void t(@NonNull String message) {
        if (toast == null) {
            throw new IllegalStateException("Toast is not initialized");
        }

        if (!TextUtils.isEmpty(message)) {
            toast.setText(message);
            toast.show();
        }
    }
}
