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
import com.anenn.core.utils.L;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * 网络状态服务类
 * Created by Anenn on 2015/6/21.
 */
public class NetworkStateService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        L.i("NetworkStateService onCreate");

        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("NetworkStateService onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        L.i("NetworkStateService onBind");

        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        L.i("NetworkStateService onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        L.i("NetworkStateService onDestroy");

        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
        }

        super.onDestroy();
    }

    // 网络状态广播接收者
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (CONNECTIVITY_ACTION.equals(action)) {
                    L.i("Network connectivity have been changed");

                    String name = NetworkManager.getConnectedTypeName(context);
                    if (!TextUtils.isEmpty(name)) {
                        L.i("The current network name : " + name);
                    } else {
                        L.i("No found a useful network");
                    }
                }
            }
        }
    };
}
