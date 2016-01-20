package com.anenn.core.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anenn.core.utils.ImageLoaderUtil;

/**
 * Created by Anenn on 15-7-23.
 */
public final class ViewHolderHelper implements View.OnClickListener, View.OnLongClickListener {
    protected Context mContext;
    protected View mConvertView;
    protected final SparseArray<View> mViews;
    protected int position;
    protected RVAdapter.RVViewHolder mRVViewHolder;
    protected ItemClickListener mItemClickListener;

    public ViewHolderHelper(View convertView) {
        mContext = convertView.getContext();
        mConvertView = convertView;
        mViews = new SparseArray<>();
    }

    public void setRVViewHolder(RVAdapter.RVViewHolder rvViewHolder) {
        mRVViewHolder = rvViewHolder;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        if (mRVViewHolder != null) {
            return mRVViewHolder.getLayoutPosition();
        }
        return position;
    }

    public <T extends View> T obtainView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public ViewHolderHelper setChildClickListener(Integer... viewIds) {
        for (Integer viewId : viewIds)
            obtainView(viewId).setOnClickListener(this);
        return this;
    }

    public ViewHolderHelper setChildLongClickListener(Integer... viewIds) {
        for (Integer viewId : viewIds)
            obtainView(viewId).setOnLongClickListener(this);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemChildClickListener(v, getPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mItemClickListener != null) {
            return mItemClickListener.onItemChildLongClickListener(v, getPosition());
        }
        return false;
    }

    public ViewHolderHelper setText(int viewId, String content) {
        TextView textView = obtainView(viewId);
        textView.setText(content);
        return this;
    }

    public ViewHolderHelper setTextHTML(int viewId, String content) {
        TextView textView = obtainView(viewId);
        textView.setText(Html.fromHtml(content));
        return this;
    }

    public ViewHolderHelper setChecked(int viewId, boolean checked) {
        CheckBox checkBox = obtainView(viewId);
        checkBox.setChecked(checked);
        return this;
    }

    public ViewHolderHelper setImageResource(int viewId, int imageResId) {
        ImageView imageView = obtainView(viewId);
        imageView.setImageResource(imageResId);
        return this;
    }

    public ViewHolderHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = obtainView(viewId);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolderHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = obtainView(viewId);
        if (drawable != null)
            imageView.setImageDrawable(drawable);
        return this;
    }

    public ViewHolderHelper setImageUrl(int viewId, String url) {
        ImageView imageView = obtainView(viewId);
        ImageLoaderUtil.loadImage(url, imageView);
        return this;
    }

    public ViewHolderHelper setVisibility(int viewId, int visibility) {
        View view = obtainView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public ViewHolderHelper setTag(int viewId, Object tag) {
        View view = obtainView(viewId);
        view.setTag(tag);
        return this;
    }
}
