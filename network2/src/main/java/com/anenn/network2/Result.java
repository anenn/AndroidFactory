package com.anenn.network2;

import org.json.JSONObject;

/**
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
