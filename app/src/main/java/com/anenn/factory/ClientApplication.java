package com.anenn.factory;

import android.app.Application;

import com.anenn.imageloader.ImageLoaderUtil;

/**
 * Created by Anenn on 2015/12/31.
 */
public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        T.init(this);
//        Global.init(this);
        ImageLoaderUtil.init(this);
    }
}
