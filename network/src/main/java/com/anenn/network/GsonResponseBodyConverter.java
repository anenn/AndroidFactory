package com.anenn.network;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * JSON 转换成 Obj
 * Created by anenn on 3/27/16.
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        // 从 ResponseBody 对象中取出服务器响应的 JSON 数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
        StringBuilder sb = new StringBuilder();
        String line ;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        String result = sb.toString();
        final ResultResponse resultResponse = gson.fromJson(result, ResultResponse.class);
        if (resultResponse.isOk()) {
            try {
                // 提前取出待解析的对象的 JSON 数据
                result = new JSONObject(result).optString(ResultResponse.JSON_PARSE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gson.fromJson(result, type);
        } else {
            throw new ResultException(resultResponse.getCode(), resultResponse.getMsg());
        }
    }
}
