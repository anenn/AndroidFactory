package com.anenn.network;

import com.socks.library.KLog;

/**
 * Api 异常
 * Created by anenn on 3/27/16.
 */
public class ApiException extends Exception {

    public static final int UNKNOWN = 0;
    public static final int NETWORK_ERROR = -1;
    public static final int PARSE_ERROR = -2;

    private final int errorCode;
    private String errorMessage;

    public ApiException(Throwable throwable, int errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String msg) {
        KLog.e(msg + "(errorCode: " + errorCode + ")");
        this.errorMessage = msg;
    }
}

