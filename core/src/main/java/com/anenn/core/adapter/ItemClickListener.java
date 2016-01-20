package com.anenn.core.adapter;

import android.view.View;

/**
 * Created by Anenn on 15-7-23.
 */
public interface ItemClickListener {

    void onItemChildClickListener(View view, int position);

    boolean onItemChildLongClickListener(View view, int position);

    void onLoadMore();
}