package com.anenn.core.app;

import android.content.Context;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * App Config
 * Storing some info of app's config.
 * Created by Anenn on 15-7-23.
 */
public class AppConfig {
    private static final String APP_CONFIG = "config";

    private Context mContext;
    private static AppConfig mAppConfig;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (mAppConfig == null) {
            synchronized (AppConfig.class) {
                if (mAppConfig == null) {
                    mAppConfig = new AppConfig();
                }
            }
        }
        return mAppConfig;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * According the key, return the value
     *
     * @param key 关键字
     * @return 关键字对应的值
     */
    public String getValue(String key) {
        Properties properties = getProperties();
        return properties != null ? properties.getProperty(key) : null;
    }

    /**
     * Saving the properties
     *
     * @param properties 属性对象
     */
    public void setValue(Properties properties) {
        Properties pros = getProperties();
        if (pros != null) {
            pros.putAll(properties);
            setProperties(pros);
        }
    }

    /**
     * Removing the info according to the key
     *
     * @param keys 可变长的关键字
     */
    public void removeValue(String... keys) {
        Properties properties = getProperties();
        if (properties != null) {
            for (String key : keys) {
                properties.remove(key);
            }
            setProperties(properties);
        }
    }

    /**
     * Saving the key-value
     *
     * @param key   关键字
     * @param value 关键字对应的值
     */
    public void setKV(String key, Object value) {
        Properties properties = getProperties();
        if (properties != null && !TextUtils.isEmpty(key)
                && value != null) {
            properties.put(key, value);
            setValue(properties);
        }
    }

    /**
     * Get Properties
     *
     * @return 属性对象
     */
    public Properties getProperties() {
        FileInputStream input = null;
        Properties properties = new Properties();
        try {
            File dir = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            input = new FileInputStream(dir.getPath() + File.separator + APP_CONFIG);
            properties.load(input);
        } catch (IOException e) {
            KLog.d(e.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                KLog.d(e.getMessage());
            }
        }
        return properties;
    }

    /**
     * Save Properties
     *
     * @param properties 属性对象
     */
    public void setProperties(Properties properties) {
        FileOutputStream out = null;
        File dir = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
        File file = new File(dir, APP_CONFIG);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            properties.store(out, null);
            out.flush();
        } catch (IOException e) {
            KLog.d(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                KLog.d(e.getMessage());
            }
        }
    }
}
