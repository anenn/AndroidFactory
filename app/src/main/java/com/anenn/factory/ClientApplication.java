package com.anenn.factory;

import android.app.Application;

import com.anenn.core.Global;
import com.anenn.core.utils.ImageLoaderUtil;
import com.anenn.core.utils.T;

/**
 * Created by Anenn on 2015/12/31.
 */
public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        T.init(this);
        Global.init(this);
        ImageLoaderUtil.init(this);
    }
}
