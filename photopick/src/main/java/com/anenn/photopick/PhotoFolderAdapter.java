package com.anenn.photopick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 图片文件夹适配器
 * Created by Anenn on 2015/12/31.
 */
public class PhotoFolderAdapter extends BaseAdapter {

    // 图片文件夹数据集
    private ArrayList<ImageInfoExtra> mFolderData = new ArrayList<>();
    // 被选中的文件夹的名称
    private String mSelect = "";

    public PhotoFolderAdapter(ArrayList<ImageInfoExtra> mFolderData) {
        this.mFolderData = mFolderData;
    }

    public String getSelect() {
        return mSelect;
    }

    public void setSelect(int pos) {
        if (pos >= getCount()) {
            return;
        }

        mSelect = mFolderData.get(pos).getName();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolderData.size();
    }

    @Override
    public Object getItem(int position) {
        return mFolderData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_photopick_folder, parent, false);
            holder = new ViewHolder();
            holder.foldIcon = (ImageView) convertView.findViewById(R.id.foldIcon);
            holder.foldName = (TextView) convertView.findViewById(R.id.foldName);
            holder.photoCount = (TextView) convertView.findViewById(R.id.photoCount);
            holder.check = convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageInfoExtra data = mFolderData.get(position);
        String uri = data.getPath();
        int count = data.getCount();

        holder.foldName.setText(data.getName());
        holder.photoCount.setText(String.format("%d张", count));

        ImageLoader.getInstance().displayImage(ImageInfo.pathAddPreFix(uri), holder.foldIcon,
                PhotoPickActivity.optionsImage);

        if (data.getName().equals(mSelect)) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView foldIcon;
        TextView foldName;
        TextView photoCount;
        View check;
    }
}
