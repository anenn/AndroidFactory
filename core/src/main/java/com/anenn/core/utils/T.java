package com.anenn.core.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by Anenn on 15-7-23.
 */
public class T {
    private static Toast toast;

    public static void init(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static void t(@NonNull String message) {
        if (toast == null) {
            throw new IllegalStateException("Toast is not initialized");
        }
        toast.setText(message);
        toast.show();
    }
}
