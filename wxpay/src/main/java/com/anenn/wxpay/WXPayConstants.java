package com.anenn.wxpay;

public class WXPayConstants {

    // APP_ID: 请同时修改 AndroidManifest.xml 里面 .PayActivity 里的属性<data android:scheme="xxx"/>
    private static String mAppId;

    public static String getAppId() {
        return mAppId;
    }

    public static void setAppId(String appId) {
        mAppId = appId;
    }
}
