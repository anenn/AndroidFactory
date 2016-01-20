package com.anenn.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Anenn on 15-7-23
 */
public final class DialogUtil {

    private static void dialogTitleLineColor(Context context, Dialog dialog, int color) {
        String dividers[] = {
                "android:id/titleDividerTop", "android:id/titleDivider"
        };

        for (int i = 0; i < dividers.length; ++i) {
            int dividerId = context.getResources().getIdentifier(dividers[i], null, null);
            View divider = dialog.findViewById(dividerId);
            if (divider != null) {
                divider.setBackgroundColor(color);
            }
        }
    }

    public static void dialogTitleLineColor(Context context, Dialog dialog) {
        if (dialog != null) {
            dialogTitleLineColor(context, dialog, context.getResources().getColor(android.R.color.holo_blue_dark));
        }
    }
}
