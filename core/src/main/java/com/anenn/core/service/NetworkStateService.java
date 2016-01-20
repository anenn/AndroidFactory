package com.anenn.core.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.anenn.core.manager.NetworkManager;
import com.socks.library.KLog;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * 网络状态服务类
 * Created by Anenn on 2015/6/21.
 */
public class NetworkStateService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i("NetworkStateService onCreate");
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.i("NetworkStateService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        KLog.i("NetworkStateService onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        KLog.i("NetworkStateService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.i("NetworkStateService onDestroy");
        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
        }
    }

    // 网络状态广播接收者
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CONNECTIVITY_ACTION)) {
                KLog.i("网络状态已改变");
                String name = NetworkManager.getConnectedTypeName(context);
                if (!TextUtils.isEmpty(name)) {
                    KLog.i("当前网络名称: " + name);
                } else {
                    KLog.i("没有可用的网络");
                }
            }
        }
    };
}
