package com.anenn.network2;

import org.json.JSONObject;

/**
 * 后台返回 json 数据初步预判
 * 前端与后台商定的 json 数据格式为:
 * {
 *     "code" : 1000,
 *     "msg": "success",
 *     "payload" : {
 *         ...
 *     }
 * }
 * 其中, code 为 1000 表示本次请求是正常的, payload 对应的可以是一个对象或一个对象数组
 * Created by Anenn on 15-7-23.
 */
public class Result {
    private static final int RESULT_CODE = 1000;

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean ok() {
        return code == RESULT_CODE;
    }

    public static Result parse(JSONObject obj) {
        Result result = new Result();
        result.code = obj.optInt("code");
        result.message = obj.optString("msg");
        return result;
    }
}
