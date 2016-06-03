package com.anenn.factory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.anenn.core.adapter.LVAdapter;
import com.anenn.factory.R;

import java.util.List;

/**
 * Copyright Youdar, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * <p>
 * Created by anenn <anennzxq@gmail.com> on 5/4/16.
 */
public class DataAdapter extends LVAdapter<String> {

    public DataAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    protected View getItemView(int position, ViewGroup parent) {
        return mInflater.inflate(R.layout.view_adapter, parent, false);
    }

    @Override
    protected void initItemViewData(LVViewHolder holder, String data, int position) {

        holder.getViewHolderHelper().setText(R.id.tvShow, data);
    }
}
