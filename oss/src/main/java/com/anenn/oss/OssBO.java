package com.anenn.oss;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件上传、下载操作实体类
 * Created by Anenn on 2016/1/3.
 */
public class OssBO implements Parcelable {
    private int id; // ID
    private String uploadFilePath; // 上传文件的路径
    private String downloadFilePath; // 下载文件的路径
    private String extra; // 额外信息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    protected OssBO(Parcel in) {
        id = in.readInt();
        uploadFilePath = in.readString();
        downloadFilePath = in.readString();
        extra = in.readString();
    }

    public static final Creator<OssBO> CREATOR = new Creator<OssBO>() {
        @Override
        public OssBO createFromParcel(Parcel in) {
            return new OssBO(in);
        }

        @Override
        public OssBO[] newArray(int size) {
            return new OssBO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uploadFilePath);
        dest.writeString(downloadFilePath);
        dest.writeString(extra);
    }
}
