package com.anenn.core.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络管理类
 * Created by Anenn on 2015/6/21.
 */
public class NetworkManager {

    private static final String CMWAP = "cmwap";
    private static final String CMNET = "cmnet";

    public enum NetworkType {
        DISCONNECT(-1),
        WIFI(1),
        CMWAP(2),
        CMNET(3);

        private int type;

        public int getValue() {
            return type;
        }

        NetworkType(int type) {
            this.type = type;
        }

        public static NetworkType setValue(int type) {
            for (NetworkType networkType : values()) {
                if (networkType.getValue() == type) {
                    return networkType;
                }
            }
            return null;
        }
    }

    /**
     * 获取网络连接信息
     *
     * @param context 应用上下文
     * @param type    网络连接类型
     * @return 网络连接信息
     */
    private static NetworkInfo getNetworkInfo(Context context, int type) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        // type = NetworkType.DISCONNECT.getValue() : 表示没有指定想要获取的网络连接类型，而是直接返回当前有效的网络
        if (type == NetworkType.DISCONNECT.getValue()) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } else {
            networkInfo = connectivityManager.getNetworkInfo(type);
        }
        return networkInfo;
    }

    /**
     * 判断是否网络连接
     *
     * @param context 应用上下文
     * @return boolean true表示有网络连接，反之亦然
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = getNetworkInfo(context, NetworkType.DISCONNECT.getValue());
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断Wifi网络是否可用
     *
     * @param context 应用上下文
     * @return boolean true表示 wifi 连接，反之亦然
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context 应用上下文
     * @return boolean true表示有移动网络连接，反之亦然
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     * 网络连接类型type :
     * -1 (无网络连接)
     * 1 (WIFI网络)
     * 2 (CMWAP网络)
     * 3 (CMNET网络)
     *
     * @param context 应用上下文
     * @return int 当前网络的连接类型
     */
    public static int getConnectedType(Context context) {
        int typeValue = NetworkType.DISCONNECT.getValue();

        if (context != null) {
            NetworkInfo networkInfo = getNetworkInfo(context, typeValue);
            if (networkInfo != null) {
                typeValue = networkInfo.getType();
                if (typeValue == ConnectivityManager.TYPE_MOBILE) {
                    String networkExtraInfo = networkInfo.getExtraInfo().toLowerCase();
                    if (networkExtraInfo.equals(CMNET)) {
                        typeValue = NetworkType.CMNET.getValue();
                    } else if (networkExtraInfo.equals(CMWAP)) {
                        typeValue = NetworkType.CMWAP.getValue();
                    }
                } else if (typeValue == ConnectivityManager.TYPE_WIFI) {
                    typeValue = NetworkType.WIFI.getValue();
                }
            }
        }
        return typeValue;
    }

    /**
     * 获取当前网络连接的类型名称
     *
     * @param context 应用上下文
     * @return String 当前网络的名称
     */
    public static String getConnectedTypeName(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = getNetworkInfo(context, -1);
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getTypeName();
            }
        }
        return null;
    }
}
