package com.anenn.core.manager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anenn.core.R;

/**
 * Created by Anenn on 15-7-23.
 */
public abstract class BlankViewBaseManager {

    public void setBlank(int itemSize, Object object, boolean reloadable,
                         View blankViewLayout, View.OnClickListener onClick) {
        if (blankViewLayout == null) {
            return;
        } else if (itemSize != 0) {
            blankViewLayout.setVisibility(View.GONE);
            return;
        }

        final Button btnRetry = (Button) blankViewLayout.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(onClick);

        int iconId = R.drawable.ic_exception;
        String content = blankViewLayout.getContext().getString(R.string.net_load_error);
        BlankViewObject blankViewObject;
        if (!reloadable) {
            blankViewObject = updateData(object, btnRetry);
        } else {
            blankViewObject = new BlankViewObject(iconId, content);
        }

        blankViewLayout.findViewById(R.id.icon).setBackgroundResource(blankViewObject.iconId);
        ((TextView) blankViewLayout.findViewById(R.id.tvMsg)).setText(blankViewObject.content);
        blankViewLayout.setVisibility(View.VISIBLE);
    }

    protected abstract BlankViewObject updateData(Object object, Button btnRetry);

    public static class BlankViewObject {
        private int iconId;
        private String content;

        public BlankViewObject() {
        }

        public BlankViewObject(int iconId, String content) {
            this.iconId = iconId;
            this.content = content;
        }

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
