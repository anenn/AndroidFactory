package com.anenn.core.network;

import org.json.JSONObject;

/**
 * 网络请求回调接口
 * Created by Anenn on 15-7-23.
 */
public interface NetworkCallback {

    void onSuccess(int code, JSONObject response, Object data, String tag);

    void onFailure(int code, JSONObject response, Object data, String tag);

    void onError(int code, String error, Object data, String tag);
}
