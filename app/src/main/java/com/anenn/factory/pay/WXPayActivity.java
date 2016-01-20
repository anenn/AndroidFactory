package com.anenn.factory.pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.anenn.factory.R;
import com.anenn.wxpay.WXPayUtil;

/**
 * Created by Anenn on 2015/11/20.
 */
public class WXPayActivity extends AppCompatActivity {

    private TextView show;
    private WXPayUtil wxPayUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);

    }
}
