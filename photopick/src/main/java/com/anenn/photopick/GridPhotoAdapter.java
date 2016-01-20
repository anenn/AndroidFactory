package com.anenn.photopick;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.anenn.core.Global;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.anenn.photopick.PhotoPickActivity.GridViewCheckTag;

/**
 * 图片网格适配器
 * Created by Anenn on 2015/7/30.
 */
class GridPhotoAdapter extends CursorAdapter {

    protected PhotoPickActivity mActivity;
    // 布局扩充器
    protected LayoutInflater mInflater;
    // 单张图片的宽度
    private final int itemWidth;

    private OnClickListener mClickItem = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.clickPhotoItem(v);
        }
    };

    GridPhotoAdapter(Cursor cursor, boolean autoRequery, PhotoPickActivity activity) {
        super(activity, cursor, autoRequery);

        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        int spacePix = activity.getResources().getDimensionPixelSize(R.dimen.grid_item_space);
        itemWidth = (Global.sWidthPix - spacePix * 4) / 3;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.item_photo_grid, parent, false);
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemWidth;
        convertView.setLayoutParams(layoutParams);

        GridViewHolder holder = new GridViewHolder();
        holder.icon = (ImageView) convertView.findViewById(R.id.icon);
        holder.iconFore = (ImageView) convertView.findViewById(R.id.iconFore);
        holder.check = (CheckBox) convertView.findViewById(R.id.check);
        GridViewCheckTag checkTag = new GridViewCheckTag(holder.iconFore);
        holder.check.setTag(checkTag);
        holder.check.setOnClickListener(mClickItem);
        convertView.setTag(holder);

        ViewGroup.LayoutParams iconParam = holder.icon.getLayoutParams();
        iconParam.width = itemWidth;
        iconParam.height = itemWidth;
        holder.icon.setLayoutParams(iconParam);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        GridViewHolder holder = (GridViewHolder) view.getTag();

        final ImageLoader imageLoader = ImageLoader.getInstance();
        String path = ImageInfo.pathAddPreFix(cursor.getString(1));
        imageLoader.displayImage(path, holder.icon, PhotoPickActivity.optionsImage);

        ((GridViewCheckTag) holder.check.getTag()).path = path;

        boolean picked = mActivity.isPicked(path);
        holder.check.setChecked(picked);
        holder.iconFore.setVisibility(picked ? View.VISIBLE : View.INVISIBLE);
    }

    static class GridViewHolder {
        ImageView icon;
        ImageView iconFore;
        CheckBox check;
    }
}
