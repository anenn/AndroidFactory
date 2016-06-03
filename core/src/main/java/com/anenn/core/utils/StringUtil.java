package com.anenn.core.utils;

import android.text.TextUtils;

/**
 * Created by Anenn on 15-7-23.
 */
public class StringUtil {

    /**
     * 字符串转整数
     *
     * @param str      待转换的字符串
     * @param defValue 默认整数
     * @return 转换后的整数
     */
    public static int str2Int(String str, int defValue) {
        try {
            if (TextUtils.isEmpty(str))
                return defValue;
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj 待转换的对象
     * @return 转换异常返回 0
     */
    public static int obj2Int(Object obj) {
        if (obj == null)
            return 0;
        return str2Int(obj.toString(), 0);
    }

    /**
     * 字符串转长整数
     *
     * @param obj      待转换的字符串
     * @param defValue 默认长整数
     * @return 转换后的长整数
     */
    public static long str2Long(String obj, Long defValue) {
        try {
            if (TextUtils.isEmpty(obj))
                return defValue;
            return Long.parseLong(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 对象转长整数
     *
     * @param obj 待转换的对象
     * @return 转换异常返回0L
     */
    public static long obj2Long(Object obj) {
        if (obj == null)
            return 0L;
        return str2Long(obj.toString(), 0L);
    }

    /**
     * 字符串转布尔值
     *
     * @param obj      待转换的字符串
     * @param defValue 默认布尔值
     * @return 转换后布尔值
     */
    public static boolean str2Bool(String obj, Boolean defValue) {
        try {
            if (TextUtils.isEmpty(obj))
                return defValue;
            return Boolean.parseBoolean(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 对象转布尔值
     *
     * @param obj 待转换的对象
     * @return 转换异常返回false
     */
    public static boolean obj2Bool(Object obj) {
        if (obj == null)
            return false;
        return str2Bool(obj.toString(), false);
    }
}
