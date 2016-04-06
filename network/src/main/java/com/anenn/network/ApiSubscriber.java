package com.anenn.network;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 自定义订阅者
 * 主要针对 onError 的回调方法进行扩展
 * Created by anenn on 3/27/16.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    // 对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    // 出错提示
    private final String networkMsg;
    private final String parseMsg;
    private final String unknownMsg;

    public ApiSubscriber() {
        this("网络异常", "数据解析异常", "未知错误");
    }

    public ApiSubscriber(String networkMsg, String parseMsg, String unknownMsg) {
        this.networkMsg = networkMsg;
        this.parseMsg = parseMsg;
        this.unknownMsg = unknownMsg;
    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        // 获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        ApiException apiException;
        if (e instanceof HttpException) {                      // HTTP 错误
            HttpException httpException = (HttpException) e;
            apiException = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    onPermissionError(apiException);           // 权限错误，需要实现
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    apiException.setErrorMessage(networkMsg);  // 均视为网络错误
                    onError(apiException);
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            apiException = new ApiException(e, ApiException.PARSE_ERROR);
            apiException.setErrorMessage(parseMsg);            // 均视为解析错误
            onParseError(apiException);
        } else if (e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            apiException = new ApiException(e, ApiException.UNKNOWN);
            apiException.setErrorMessage(networkMsg);          // 网络错误, 如手机无有效的的网络或请求超时
            onError(apiException);
        } else if (e instanceof ResultException) {             // 服务器返回的错误
            ResultException resultException = (ResultException) e;
            apiException = new ApiException(resultException, resultException.getErrorCode());
            onResultError(apiException);
        } else {
            apiException = new ApiException(e, ApiException.UNKNOWN);
            apiException.setErrorMessage(unknownMsg);          // 未知错误
            onError(apiException);
        }
    }

    /**
     * 错误回调
     */
    protected void onError(ApiException ex) {
    }

    /**
     * 权限错误
     */
    protected void onPermissionError(ApiException ex) {
    }

    /**
     * 解析错误
     */
    protected void onParseError(ApiException ex) {
    }

    /**
     * 服务器返回的错误
     */
    protected void onResultError(ApiException ex) {
    }

    @Override
    public void onCompleted() {

    }
}
