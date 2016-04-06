package com.anenn.network2;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by Anenn on 15-7-23.
 */
public class NetworkContext {
    private static final int DEFAULT_TIMEOUT = 30000;

    private Context mContext;
    private AsyncHttpClient mHttpClient;
    private HashMap<String, Boolean> mProcessing;

    public NetworkContext(Context context) {
        mContext = context;
        mHttpClient = CustomAsyncHttpClient.createClient(context);
        mProcessing = new HashMap<>();
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        mHttpClient.setCookieStore(cookieStore);
        mHttpClient.setTimeout(DEFAULT_TIMEOUT);
    }

    public AsyncHttpClient getHttpClient() {
        return mHttpClient;
    }

    public final void clearCookie(Context context) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        myCookieStore.clear();
        mHttpClient.setCookieStore(myCookieStore);
    }

    public final String getCookie(Header[] headers) {
        String cookie = null;
        for (Header header : headers) {
            if (header.getName().equals("Set-Cookie")) {
                cookie = header.getValue();
            }
        }
        return cookie;
    }

    private JsonHttpResponseHandler createRequestHandler(final Object data, final String tag,
                                                         final NetworkCallback callback) {
        if (mProcessing.containsKey(tag) && mProcessing.get(tag)) {
            return null;
        }
        mProcessing.put(tag, true);

        final JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                KLog.e("URL: " + tag + "\nstatusCode: " + statusCode);
                KLog.json(response.toString());
                Result result = Result.parse(response);
                if (result.ok()) {
                    callback.onSuccess(result.getCode(), response, data, tag);
                } else {
                    callback.onFailure(result.getCode(), response, data, tag);
                }
                mProcessing.put(tag, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                KLog.e("URL: " + tag + "\nstatusCode: " + statusCode + ", responseString: "
                        + responseString + ", throwable: " + throwable);
                callback.onError(statusCode, responseString, data, tag);
                mProcessing.put(tag, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                KLog.e("URL: " + tag + "\nstatusCode: " + statusCode + ", throwable: "
                        + throwable + ", errorResponse: " + errorResponse);
                callback.onError(statusCode, null, data, tag);
                mProcessing.put(tag, false);
            }
        };
        return handler;
    }

    public final void get(String url, String tag, NetworkCallback callback) {
        get(url, tag, null, callback);
    }

    public final void get(String url, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.get(mContext, url, handler);
        }
    }

    public final void post(String url, RequestParams params, String tag, NetworkCallback callback) {
        post(url, params, tag, null, callback);
    }

    public final void post(String url, RequestParams params, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.post(mContext, url, params, handler);
        }
    }

    public final void post(String url, HttpEntity entity, String contentType, String tag, NetworkCallback callback) {
        post(url, entity, contentType, tag, null, callback);
    }

    public final void post(String url, HttpEntity entity, String contentType, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.post(mContext, url, entity, contentType, handler);
        }
    }

    public final void put(String url, RequestParams params, String tag, NetworkCallback callback) {
        put(url, params, tag, null, callback);
    }

    public final void put(String url, RequestParams params, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.put(mContext, url, params, handler);
        }
    }

    public final void put(String url, HttpEntity entity, String contentType, String tag, NetworkCallback callback) {
        put(url, entity, contentType, tag, null, callback);
    }

    public final void put(String url, HttpEntity entity, String contentType, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.put(mContext, url, entity, contentType, handler);
        }
    }

    public final void del(String url, String tag, NetworkCallback callback) {
        del(url, tag, null, callback);
    }

    public final void del(String url, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.delete(mContext, url, handler);
        }
    }

    public final void del(String url, RequestParams params, String tag, NetworkCallback callback) {
        del(url, params, tag, null, callback);
    }

    public final void del(String url, RequestParams params, String tag, Object data, NetworkCallback callback) {
        JsonHttpResponseHandler handler = createRequestHandler(data, tag, callback);
        if (handler != null) {
            mHttpClient.delete(url, params, handler);
        }
    }
}
