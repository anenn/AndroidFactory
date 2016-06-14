package com.anenn.factory.pickphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.anenn.core.utils.L;
import com.anenn.factory.R;
import com.anenn.imageloader.ImageLoaderUtil;
import com.anenn.photopick.PhotoManager;

import java.util.List;

/**
 * Created by Anenn on 2015/12/31.
 */
public class ImageShowActivity extends AppCompatActivity {

    private ImageView ivShow;

    private PhotoManager photoManager;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initView();
        initData();
    }

    private void initView() {
        ivShow = (ImageView) findViewById(R.id.ivShow);
    }

    private void initData() {
        photoManager = new PhotoManager(this) {
            @Override
            public void obtainMultiPhoto(List<String> imageInfoList) {
                for (String imageInfo : imageInfoList) {
                    L.d(imageInfo);
                }
            }

            @Override
            public void obtainSinglePhoto(String path) {
                L.d(path);
                url = "file://" + path;
                ImageLoaderUtil.displayImage(url, ivShow);
            }

            @Override
            public void viewImagesCallback(List<String> uriList) {
            }
        };
    }

    public void toChoice(View view) {
        photoManager.showSingleDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (photoManager != null) {
            photoManager.onDestroy();
        }
        super.onDestroy();
    }
}
