package com.anenn.factory.loadimage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.anenn.factory.R;
import com.anenn.imageloader.ImageLoaderUtil;

/**
 * ImageLoaderUtil 工具使用介绍, 记得在 Application 的 onCreate 方法中初始化 ImageLoaderUtil.init(context);
 * Created by Anenn on 6/7/16.
 */
public class LoadImageActivity extends AppCompatActivity {

    private ImageView ivShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);

        initView();
    }

    private void initView() {
        ivShow = (ImageView) findViewById(R.id.ivShow);
    }

    public void loadImage(View view) {
        ImageLoaderUtil.displayImage("http://a.codekk.com/images/weixin-codekk-160.jpg", ivShow);
    }
}
