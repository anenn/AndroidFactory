package com.anenn.network;

/**
 *
 * Created by anenn on 3/27/16.
 */
public class ResultResponse {

    // 与服务器约定的请求成功的状态码
    private static final int SUCCESS_CODE = 100;

    // 以 "payload" 为键的对象解析, 自己可以根据需要进行更改, 如服务器返回的数据格式为:
    /**
        "{
            "code"       : 1000,
            "msg"        : "success",
            "payload"    : {
                "id"     : 1,
                "name"   : "anenn",
                "avatar" : "http://www.qiniu.com/1234.png"
            }
        }"
     */
    public static final String JSON_PARSE = "payload";

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOk() {
        return SUCCESS_CODE == code;
    }
}
