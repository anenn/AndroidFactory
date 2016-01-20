package com.anenn.photopick;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 图片信息实体类
 * Created by Anenn on 2015/7/29.
 */
public class ImageInfo implements Parcelable {
    private static final String PREFIX = "file://";

    private String path;

    public ImageInfo(String path) {
        this.path = path;
    }

    public ImageInfo(String path, boolean addPrefix) {
        if (addPrefix) {
            this.path = pathAddPreFix(path);
        } else {
            this.path = path;
        }
    }

    /**
     * 将源文件转为本地文件
     *
     * @param path 源文件路径
     * @return 目标文件路径
     */
    public static String pathAddPreFix(@NonNull String path) {
        if (!isLocalFile(path)) {
            path = PREFIX + path;
        }
        return path;
    }

    /**
     * 获取本地文件
     *
     * @param pathSrc 源文件路径
     * @return 目标文件的路径
     */
    public static String getLocalFilePath(@NonNull String pathSrc) {
        String pathDesc = pathSrc;
        if (isLocalFile(pathDesc)) {
            pathDesc = pathDesc.substring(PREFIX.length(), pathDesc.length());
        }
        return pathDesc;
    }

    /**
     * 判断文件是否为本地文件
     *
     * @param path 文件路径
     * @return true 表示为本地文件，反之亦然
     */
    public static boolean isLocalFile(String path) {
        return path.startsWith(PREFIX);
    }

    /**
     * 通过文件头来判断是否gif文件
     */
    public static boolean isGifByFile(File file) {
        try {
            int length = 10; // 文件头的长度
            InputStream is = new FileInputStream(file);
            byte[] data = new byte[length];
            is.read(data);
            String type = getType(data);
            is.close();

            if (type.equals("gif")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取文件的类型
     *
     * @param data 文件头信息
     * @return 文件类型
     */
    private static String getType(byte[] data) {
        String type = "";
        if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
            type = "png";
        } else if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            type = "gif";
        } else if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                && data[9] == 'F') {
            type = "jpg";
        }
        return type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ImageInfo that = (ImageInfo) obj;
        return !(path != null ? !path.equals(that.getPath()) : that.getPath() != null);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    protected ImageInfo(Parcel in) {
        path = in.readString();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
    }
}
