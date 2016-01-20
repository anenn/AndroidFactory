package com.anenn.photopick;

/**
 * 图片信息实体类
 * Created by Anenn on 15/5/6.
 */
public class ImageInfoExtra {
    private ImageInfo mImageInfo; // 图片信息对象
    private String mName; // 图片所在文件夹的名称
    private int mCount; // 文件夹中图片的总数目

    public ImageInfoExtra(String name, ImageInfo imageInfo, int count) {
        mImageInfo = imageInfo;
        mName = name;
        mCount = count;
    }

    public String getPath() {
        return mImageInfo.getPath();
    }

    public int getCount() {
        return mCount;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ImageInfoExtra that = (ImageInfoExtra) obj;
        return mCount == that.mCount && mImageInfo.equals(that.mImageInfo);
    }

    @Override
    public int hashCode() {
        int result = mImageInfo.hashCode();
        result = 31 * result + mCount;
        return result;
    }
}
