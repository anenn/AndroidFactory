package com.anenn.network2;

import org.json.JSONObject;

/**
 * 网络请求回调接口
 * Created by Anenn on 15-7-23.
 */
public interface NetworkCallback {

    void onSuccess(int stateCode, JSONObject response, Object data, String tag);

    void onFailure(int stateCode, JSONObject response, Object data, String tag);

    void onError(int stateCode, String error, Object data, String tag);
}
