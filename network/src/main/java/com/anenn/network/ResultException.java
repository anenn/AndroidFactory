package com.anenn.network;

/**
 * 自定义异常
 * Created by anenn on 3/27/16.
 */
public class ResultException extends RuntimeException {

    private int errorCode;

    public ResultException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return getMessage();
    }
}
