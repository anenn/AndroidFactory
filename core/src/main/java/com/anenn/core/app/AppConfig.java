package com.anenn.core.app;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anenn.core.utils.L;

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
public final class AppConfig {
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
    public String getValue(@NonNull String key) {
        Properties properties = getProperties();
        return properties != null ? properties.getProperty(key) : null;
    }

    /**
     * Saving the properties
     *
     * @param pros 属性对象
     */
    public void setValues(@NonNull Properties pros) {
        Properties properties = getProperties();
        properties.putAll(pros);
        setProperties(properties);
    }

    /**
     * Removing the info according to the key
     *
     * @param keys 可变长的关键字
     */
    public void removeValues(String... keys) {
        Properties properties = getProperties();
        for (String key : keys) {
            properties.remove(key);
        }
        setProperties(properties);
    }

    /**
     * Saving the key-value
     *
     * @param key   关键字
     * @param value 关键字对应的值
     */
    public void setKV(@NonNull String key, @NonNull Object value) {
        Properties properties = getProperties();
        properties.put(key, value);
        setValues(properties);
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
            File dirFile = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File configFile = new File(dirFile, APP_CONFIG);
            if (configFile.exists()) {
                input = new FileInputStream(configFile);
                properties.load(input);
            }
        } catch (IOException e) {
            L.e(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    L.e(e.getMessage());
                }
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
        FileOutputStream output = null;
        File dirFile = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
        File configFile = new File(dirFile, APP_CONFIG);
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            output = new FileOutputStream(configFile);
            properties.store(output, null);
            output.flush();
        } catch (IOException e) {
            L.e(e.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    L.e(e.getMessage());
                }
            }
        }
    }
}
