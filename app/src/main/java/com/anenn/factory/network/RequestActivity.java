package com.anenn.factory.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.anenn.factory.R;
import com.anenn.network2.NetworkCallback;
import com.anenn.network2.NetworkContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Anenn on 6/5/16.
 */
public class RequestActivity extends AppCompatActivity implements NetworkCallback {

    private NetworkContext networkContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        initData();
    }

    private void initData() {
        networkContext = new NetworkContext(this);
    }

    public void request(View view) {
        networkContext.get("http://www.weather.com.cn/adat/sk/101110101.html", "weather", this);
    }

    @Override
    public void onSuccess(int stateCode, JSONObject response, Object data, String tag) {
    }

    @Override
    public void onFailure(int stateCode, JSONObject response, Object data, String tag) {
        // 由于这里是使用别人第三方的请求接口,返回的数据格式并不是该网络请求库所能识别的,所以会进入 onFailure 方法
        String result = "{\n" +
                "\t\"code\" : 1000,\n" +
                "\t\"msg\" : \"success\",\n" +
                "\t\"payload\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"city\" : \"西安\",\n" +
                "\t\t\t\"cityid\" : \"101110101\",\n" +
                "\t\t\t\"temp\" : \"9\",\n" +
                "\t\t\t\"WD\" : \"东北风\",\n" +
                "\t\t\t\"WS\" : \"2级\",\n" +
                "\t\t\t\"SD\" : \"78%\",\n" +
                "\t\t\t\"WSE\" : \"2\",\n" +
                "\t\t\t\"time\" : \"10:00\",\n" +
                "\t\t\t\"isRadar\" : \"1\",\n" +
                "\t\t\t\"Radar\" : \"JC_RADAR_AZ9290_JB\",\n" +
                "\t\t\t\"njd\" : \"5100\",\n" +
                "\t\t\t\"qy\" : \"971\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Weather> weathers = JSON.parseObject(jsonObject.optString("payload"), new TypeReference<List<Weather>>() {
        });
        System.out.println(weathers);
    }

    @Override
    public void onError(int stateCode, String error, Object data, String tag) {
    }
}
