package com.anenn.network;

import com.google.gson.JsonParseException;
import com.socks.klog.*;
import com.socks.library.KLog;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 自定义订阅者
 * 主要针对 onNetworkError 的回调方法进行扩展
 * Created by anenn on 3/27/16.
 */
public class BaseSubscriber<T> extends Subscriber<T> {

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
    private String networkMsg;
    private String parseMsg;
    private String unknownMsg;

    public String getNetworkMsg() {
        return networkMsg;
    }

    public void setNetworkMsg(String networkMsg) {
        this.networkMsg = networkMsg;
    }

    public String getParseMsg() {
        return parseMsg;
    }

    public void setParseMsg(String parseMsg) {
        this.parseMsg = parseMsg;
    }

    public String getUnknownMsg() {
        return unknownMsg;
    }

    public void setUnknownMsg(String unknownMsg) {
        this.unknownMsg = unknownMsg;
    }

    public BaseSubscriber() {
        this("Network error", "Data parse error", "Unknown error");
    }

    public BaseSubscriber(String networkMsg, String parseMsg, String unknownMsg) {
        this.networkMsg = networkMsg;
        this.parseMsg = parseMsg;
        this.unknownMsg = unknownMsg;
    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;

        KLog.e(e.toString());

        // 获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();

            KLog.e(e.toString());
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
                    onNetworkError(apiException);
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
            onNetworkError(apiException);
        } else if (e instanceof ResultException) {             // 服务器返回的错误
            ResultException resultException = (ResultException) e;
            apiException = new ApiException(resultException, resultException.getErrorCode());
            apiException.setErrorMessage(resultException.getErrorMsg());
            onResultError(apiException);
        } else {
            apiException = new ApiException(e, ApiException.UNKNOWN);
            apiException.setErrorMessage(unknownMsg);          // 未知错误
            onNetworkError(apiException);
        }
    }

    /**
     * 错误回调
     */
    protected void onNetworkError(ApiException ex) {
        KLog.e(ex != null ? ex.getErrorMessage() : "Api onNetworkError");
    }

    /**
     * 权限错误
     */
    protected void onPermissionError(ApiException ex) {
        KLog.e(ex != null ? ex.getErrorMessage() : "Api onPermissionError");
    }

    /**
     * 解析错误
     */
    protected void onParseError(ApiException ex) {
        KLog.e(ex != null ? ex.getErrorMessage() : "Api onParseError");
    }

    /**
     * 服务器返回的错误
     */
    protected void onResultError(ApiException ex) {
        KLog.e(ex != null ? ex.getErrorMessage() : "Api onResultError");
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onCompleted() {
    }
}
