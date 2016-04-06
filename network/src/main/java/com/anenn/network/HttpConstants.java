package com.anenn.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Locale;

/**
 * Created by anenn on 3/27/16.
 */
public class HttpConstants {

    // 服务器地址
    public static final String END_POINT = "";
    // 连接超时30s
    public static final int CONNECT_TIME_OUT = 30000;
    // 读取超时10m
    public static final int READ_TIME_OUT = 600000;
    // 写入超时10m
    public static final int WRITE_TIME_OUT = 600000;
    // 缓存路径, 默认路径为: /data/data/packageName/cache/HttpCache
    public static final String CACHE_FILE_NAME = "HttpCache";
    // 缓存大小: 20MB
    public static final int CACHE_SIZE = 1024 * 1024 * 20;
    // Http Content-Type
    public static final String CONTENT_TYPE = "application/json";
    // Http Token
    public static String TOKEN = "";

    /**
     * 获取 UserAgent
     *
     * @param context 应用上下文
     * @return UserAgent
     */
    public static String getUserAgent(Context context) {
        final String versionName = getVersionName(context);
        final StringBuilder builder = new StringBuilder()
                .append("Mozilla/5.0(Android ")
                .append(Build.VERSION.RELEASE)
                .append("; ")
                .append(Locale.getDefault().getLanguage())
                .append("-")
                .append(Locale.getDefault().getCountry())
                .append("; ")
                .append(Build.BRAND)
                .append(" ")
                .append(Build.MANUFACTURER)
                .append(" ")
                .append(Build.MODEL)
                .append(") Mobile App/")
                .append(versionName);
        return builder.toString();
    }

    /**
     * 返回版本号, 默认返回1.0
     *
     * @param context 应用上下文
     * @return 版本号
     */
    private static String getVersionName(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "1.0";
    }
}
