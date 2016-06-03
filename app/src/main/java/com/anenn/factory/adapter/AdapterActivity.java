package com.anenn.factory.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.anenn.factory.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Youdar, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * <p>
 * Created by anenn <anennzxq@gmail.com> on 5/4/16.
 */
public class AdapterActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);

        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add("New Data " + i);
        }
        listView = (ListView) findViewById(R.id.listView);
        DataAdapter dataAdapter = new DataAdapter(this, dataList);
        listView.setAdapter(dataAdapter);


        ImageLoaderUtil.loadImage("http://www.dorkary.com/cloud/archives/2015/11/carton.jpg", (ImageView) findViewById(R.id.ivPhone));

    }
}
