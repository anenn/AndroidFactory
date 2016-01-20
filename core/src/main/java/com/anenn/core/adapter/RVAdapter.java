package com.anenn.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Anenn on 15-7-23.
 */
public abstract class RVAdapter<T> extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;

    private List<T> mDataList;
    protected LayoutInflater mInflater;
    private View mHeaderView;
    private View mFooterView;
    private int extraCount;
    private ItemClickListener mItemClickListener;

    public RVAdapter(Context context, List<T> dataList) {
        this(context, dataList, null, null);
    }

    public RVAdapter(Context context, List<T> dataList, View headerView, View footerView) {
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
        setHeaderView(headerView);
        setFooterView(footerView);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        extraCount += hasHeaderView() ? 1 : 0;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        extraCount += hasFooterView() ? 1 : 0;
    }

    public boolean hasHeaderView() {
        return mHeaderView != null;
    }

    public boolean hasFooterView() {
        return mFooterView != null;
    }

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RVViewHolder holder;
        if (viewType == TYPE_HEADER) {
            holder = new RVViewHolder(mHeaderView);
        } else if (viewType == TYPE_FOOTER) {
            holder = new RVViewHolder(mFooterView);
        } else {
            holder = new RVViewHolder(getItemView(parent, viewType));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RVViewHolder holder, int position) {
        int layoutPosition = holder.getLayoutPosition();
        if ((mHeaderView != null && layoutPosition == 0) || (mFooterView != null && getItemCount() - 1 == layoutPosition)) {
            return;
        }
        holder.getViewHolderHelper().setItemClickListener(mItemClickListener);
        final int validPos = getItemCount();
        if (validPos > 0 && position <= validPos - 1)
            initItemViewData(holder, mDataList.get(hasHeaderView() ? --position : position), layoutPosition);
    }

    protected abstract View getItemView(ViewGroup viewGroup, int viewType);

    protected abstract void initItemViewData(RVViewHolder viewHolder, T data, int position);

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size() + extraCount;
        }
        return extraCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        } else if (mFooterView != null && getItemCount() - 1 == position) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addItem(int position, T data) {
        mDataList.add(position, data);
        notifyItemChanged(position);
    }

    public void delItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(int position, T data) {
        mDataList.set(position, data);
        notifyItemChanged(position);
    }

    public static class RVViewHolder extends RecyclerView.ViewHolder {
        private ViewHolderHelper mViewHolderHelper;

        public RVViewHolder(View itemView) {
            super(itemView);
            mViewHolderHelper = new ViewHolderHelper(itemView);
            mViewHolderHelper.setRVViewHolder(this);
        }

        public ViewHolderHelper getViewHolderHelper() {
            return mViewHolderHelper;
        }
    }
}
