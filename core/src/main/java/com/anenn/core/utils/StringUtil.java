package com.anenn.core.utils;

import android.text.TextUtils;

/**
 * Created by Anenn on 15-7-23.
 */
public class StringUtil {

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
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
     * @param obj
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
     * @param obj
     * @param defValue
     * @return
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
     * @param obj
     * @return
     */
    public static long obj2Long(Object obj) {
        if (obj == null)
            return 0L;
        return str2Long(obj.toString(), 0L);
    }

    /**
     * 字符串转布尔值
     *
     * @param obj
     * @param defValue
     * @return
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
     * @param obj
     * @return
     */
    public static boolean obj2Bool(Object obj) {
        if (obj == null)
            return false;
        return str2Bool(obj.toString(), false);
    }
}
