package com.anenn.factory.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anenn.factory.R;
import com.anenn.photopick.ImagePagerActivity;
import com.anenn.photopick.PhotoManager;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Anenn on 2015/12/31.
 */
public class ImageShowActivity extends AppCompatActivity {

    private PhotoManager photoManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoManager = new PhotoManager(this) {
            @Override
            public void obtainMultiPhoto(List<String> imageInfoList) {
                for (String imageInfo : imageInfoList) {
                    KLog.d(imageInfo);
                }
            }

            @Override
            public void obtainSinglePhoto(String path) {
                KLog.d(path);
                url = "file://" + path;
            }

            @Override
            public void viewImagesCallback(List<String> uriList) {
                for (String url : uriList) {
                    KLog.d(url);
                }
            }
        };
    }

    private String url = "https://dn-coding-net-production-pp.qbox.me/e887561c-868b-4a41-a4dc-beadc16c454c.gif";

    public void toShow(View view) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra("SINGLE_URI", url);
        intent.putExtra("EDITABLE", true);
        startActivity(intent);
    }

    public void toChoice(View view) {
        photoManager.camera();
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
