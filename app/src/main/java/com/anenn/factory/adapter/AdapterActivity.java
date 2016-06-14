package com.anenn.factory.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.anenn.factory.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anenn <anennzxq@gmail.com> on 5/4/16.
 */
public class AdapterActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);

        initView();
        initData();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add("New Data " + i);
        }

        DataAdapter dataAdapter = new DataAdapter(this, dataList);
        listView.setAdapter(dataAdapter);
    }

}
