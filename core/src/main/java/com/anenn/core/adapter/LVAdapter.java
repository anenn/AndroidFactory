package com.anenn.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anenn on 15-7-23.
 */
public abstract class LVAdapter<T> extends BaseAdapter {

    private List<T> mDataList;
    protected LayoutInflater mInflater;
    protected List<LVViewHolder> mViewHolder = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public LVAdapter(Context context, List<T> dataList) {
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public int getCount() {
        if (mDataList != null)
            return mDataList.size();
        return 0;
    }

    @Override
    public T getItem(int position) {
        return mDataList == null ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LVViewHolder holder;
        if (convertView == null) {
            convertView = getItemView(position, parent);
            holder = new LVViewHolder(convertView);
            mViewHolder.add(holder);
            convertView.setTag(holder);
        } else {
            holder = (LVViewHolder) convertView.getTag();
        }

        final ViewHolderHelper viewHolderHelper = holder.getViewHolderHelper();
        viewHolderHelper.setPosition(position); // 确定点击的Item位置
        viewHolderHelper.setItemClickListener(mItemClickListener);
        final int validPos = getCount();
        if (validPos > 0 && position <= validPos - 1)
            initItemViewData(holder, getItem(position), position);

        // 加载更多
        if (position == validPos - 1) {
            if (mItemClickListener != null)
                mItemClickListener.onLoadMore();
        }

        return convertView;
    }

    protected abstract View getItemView(int position, ViewGroup parent);

    protected abstract void initItemViewData(LVViewHolder holder, T data, int position);

    public class LVViewHolder {
        private ViewHolderHelper mViewHolderHelper;

        public LVViewHolder(View view) {
            mViewHolderHelper = new ViewHolderHelper(view);
        }

        public ViewHolderHelper getViewHolderHelper() {
            return mViewHolderHelper;
        }
    }
}
