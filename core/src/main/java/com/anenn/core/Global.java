package com.anenn.core;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Anenn on 15-7-23.
 */
public class Global {
    // 邮箱正则表达式
    public static final String EMAIL_REGEXP = "^[\\w\\.]+@[\\w\\.]+\\.[\\w\\.]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);
    // 手机号码正则表达式
    public static final String PHONE_REGEXP = "^1\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEXP);

    private static Context mContext;
    public static float sScale;
    public static int sWidthPix;
    public static int sHeightPix;
    private static String versionName;

    public static void init(Context context) {
        mContext = context;
        sWidthPix = context.getResources().getDisplayMetrics().widthPixels;
        sHeightPix = context.getResources().getDisplayMetrics().heightPixels;
        sScale = context.getResources().getDisplayMetrics().scaledDensity;

        getPackageInfo();
    }

    /**
     * 获取包信息
     */
    private static void getPackageInfo() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断应用是否在后台
     *
     * @param context 应用上下文
     * @return true表示当前应用在前台，反之亦然
     */
    public static boolean isAppOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String appPackageName = context.getPackageName();
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (!runningTaskInfos.isEmpty()) {
            String topAppPackageName = runningTaskInfos.get(0).topActivity.getPackageName();
            if (!appPackageName.equals(topAppPackageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断应用进程是否存活
     *
     * @param context 应用上下文
     * @return true 表示应用进程没被系统回收, 反之亦然
     */
    public static boolean isAppAlive(Context context) {
        String packageName = context.getPackageName();
        return isAppAlive(context, packageName);
    }

    /**
     * 判断应用进程是否存活
     *
     * @param context     应用上下文
     * @param packageName 包名
     * @return true 表示应用进程没被回收,反之亦然
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取应用版本名
     *
     * @return 版本名称
     */
    public static String getVersionName() {
        return versionName;
    }

    /**
     * 控制输入法的显示隐藏状态
     *
     * @param context 应用上下文
     * @param view    参考控件
     * @param wantPop true表示弹出输入框，反之亦然
     */
    public static void popSoftKeyboard(Context context, View view, boolean wantPop) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (wantPop) {
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    public static int dpToPx(int dpValue) {
        return (int) (dpValue * mContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 判断EditText内容是否为空
     *
     * @param editText 文本输入框
     * @return 内容是否为空
     */
    public static boolean isETEmpty(EditText editText) {
        return editText == null || (editText.getText().toString().trim().length() <= 0);
    }

    /**
     * 设置文本编辑框的内容为密码模式
     *
     * @param editText 待设置的文本编辑框
     */
    public static void setETPwdMethod(EditText editText) {
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }

    /**
     * 判断是否为邮箱地址
     *
     * @param email 邮箱地址
     * @return true表示为邮箱邮箱，反之亦然
     */
    public static boolean isEmail(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }

    /**
     * 判断是否为手机号码
     *
     * @param phone 手机号码
     * @return true表示手机号码是有效的，反之亦然
     */
    public static boolean isPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).find();
    }

    /**
     * 检查密码长度是否为6-16位
     *
     * @param content 待检测的数据
     * @return true 表示有效，反之亦然
     */
    public static boolean isValidPwd(String content) {
        if (!TextUtils.isEmpty(content)) {
            int length = content.length();
            if (length >= 6 && length <= 16) {
                return true;
            }
        }
        return false;
    }
}
