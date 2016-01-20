package com.anenn.core.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anenn.core.R;

/**
 * 列表上拉加载控件
 * Created by Anenn on 2015/7/23.
 */
public class LoadMoreView extends FrameLayout {

    private View mParentLayout;
    private View mLoadingLayout;
    private TextView mFailedView;

    private LoadMore loadMore;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            initView(context);
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loadmore, this);
        mParentLayout = view.findViewById(R.id.rlLoadMore);
        mLoadingLayout = view.findViewById(R.id.llLoading);
        mFailedView = (TextView) view.findViewById(R.id.tvFailed);

        mParentLayout.setOnClickListener(onClickListener);
        mFailedView.setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof RelativeLayout) {
            } else if (v instanceof TextView) {
                loadMore.onReLoad();
            }
        }
    };

    public interface LoadMore {
        void onReLoad();
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void showNoData() {
        showMessage("没有更多的数据了");
    }

    private void showMessage(String message) {
        if (TextUtils.isEmpty(message))
            return;

        mParentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.INVISIBLE);
        mFailedView.setVisibility(View.VISIBLE);
        mFailedView.setText(message);
        mFailedView.setEnabled(false);
    }

    public final void showLoading() {
        show(true, true);
    }

    public final void showFailed() {
        show(true, false);
    }

    public final void dismiss() {
        show(false, false);
    }

    private void show(boolean show, boolean loading) {
        if (mParentLayout == null) {
            return;
        }

        if (show) {
            mParentLayout.setVisibility(View.VISIBLE);
            if (loading) {
                mLoadingLayout.setVisibility(View.VISIBLE);
                mFailedView.setVisibility(View.INVISIBLE);
            } else {
                mLoadingLayout.setVisibility(View.INVISIBLE);
                mFailedView.setVisibility(View.VISIBLE);
                mFailedView.setEnabled(true);
                mFailedView.setText(getResources().getText(R.string.net_load_failed));
            }
        } else {
            mParentLayout.setVisibility(View.INVISIBLE);
        }
    }
}
