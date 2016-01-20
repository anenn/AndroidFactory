package com.anenn.oss;

import org.json.JSONObject;

/**
 * 阿里云服务之对象存储服务配置常量
 * Created by Anenn on 2016/1/3.
 */
public class OssKV {
    public static String endPoint = ""; // 端点
    public static String accessKeyId = "";  // appId
    public static String accessKeySecret = ""; // appKey
    public static String bucket = ""; // 存储空间路径

    public static void parseJSON(JSONObject object) {
        endPoint = object.optString("endPoint");
        accessKeyId = object.optString("accessKeyId");
        accessKeySecret = object.optString("accessKeySecret");
        bucket = object.optString("bucket");
    }

    /**
     * 判断数据为有效数据
     *
     * @return true 表示为有效数据，反之亦然
     */
    public static boolean isValid() {
        return !endPoint.isEmpty()
                && !accessKeyId.isEmpty()
                && !accessKeySecret.isEmpty()
                && !bucket.isEmpty();
    }
}
