package com.anenn.core.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;

import com.anenn.core.utils.L;
import com.anenn.core.utils.T;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 应用崩溃日志拦截
 * Created by Anenn on 16/1/20
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private Context mContext;
    private static CrashHandler crashHandler;
    private UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandler getInstance() {
        if (crashHandler == null)
            synchronized (CrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                }
            }
        return crashHandler;
    }

    /**
     * CrashHandler initialized
     *
     * @param context 应用上下文
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器  
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ActivityManager.getInstance().AppExit();
        }
    }

    /**
     * 处理异常
     *
     * @param ex 异常对象
     * @return boolean 表示已处理异常，反之亦然
     */
    public boolean handleException(Throwable ex) {
        if (ex == null || mContext == null)
            return false;

        final String crashReport = getCrashReport(mContext, ex);
        L.e("HandleException" + crashReport);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                T.show("Sorry...程序出现异常, 攻城狮们会尽快解决问题!");
                Looper.loop();
            }
        }.start();

        StringBuilder postCrash = new StringBuilder(200);
        postCrash.append("Activity : ")
                .append(ActivityManager.getInstance().getActivityStack().toString())
                .append('\n')
                .append(crashReport);
        return true;
    }

    /**
     * 获取操作系统版本信息
     *
     * @return 系统版本数据
     */
    private String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机类型
     *
     * @return 手机类型数据
     */
    private String getPhoneType() {
        return android.os.Build.MODEL;
    }

    /**
     * 将崩溃日志保存到本地文件
     *
     * @param crashReport 崩溃日志
     * @return 数据保存后后的文件对象
     */
    private File save2File(final String crashReport) {
        final String fileName = "crash-" + System.currentTimeMillis() + ".txt";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "crash");
                if (!dir.exists())
                    dir.mkdir();
                File file = new File(dir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(crashReport.getBytes());
                fos.close();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 读取应用崩溃日志
     *
     * @param context 应用上下文
     * @param ex      异常对象
     * @return 日志信息
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo packageInfo = getPackageInfo(context);
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: ")
                .append(packageInfo.versionName)
                .append("(")
                .append(packageInfo.versionCode)
                .append(")\n");
        exceptionStr.append("Android: ")
                .append(android.os.Build.VERSION.RELEASE)
                .append("(")
                .append(android.os.Build.MODEL)
                .append(")\n");
        exceptionStr.append("Exception: ")
                .append(ex.getMessage())
                .append("\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (StackTraceElement element : elements) {
            exceptionStr.append(element.toString())
                    .append("\n");
        }
        return exceptionStr.toString();
    }

    /**
     * 获取应用的包信息
     *
     * @param context 应用上下文
     * @return 包信息
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
}
