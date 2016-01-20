package com.anenn.factory.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anenn.factory.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anenn on 2015/11/28.
 */
public class LineCharActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_char);
        Utils.init(this);

        ListView lv = (ListView) findViewById(R.id.listView1);
        ArrayList<ChartItem> list = new ArrayList<>();
        list.add(new LineChartItem(generateDataLine(), this));
        ChartDataAdapter cda = new ChartDataAdapter(this, list);
        lv.setAdapter(cda);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < 7; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(e1, "");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(3.5f);
        d1.setHighLightColor(Color.rgb(252, 147, 102));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 7; i++) {
            e2.add(new Entry(e1.get(i).getVal() - 30, i));
        }

        LineDataSet d2 = new LineDataSet(e2, "");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(3.5f);
        d2.setHighLightColor(Color.argb(0, 0, 0, 0)); // 点击坐标点后高亮显示的颜色
        d2.setColor(Color.rgb(252, 147, 102));
        d2.setCircleColor(Color.rgb(252, 147, 102));
        d2.setDrawValues(false); // 是否在坐标点上显示数值

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
//        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private ArrayList<String> getMonths() {
        ArrayList<String> m = new ArrayList<String>();
        m.add("");
        m.add("2");
        m.add("4");
        m.add("6");
        m.add("8");
        m.add("10");
        m.add("12");

        return m;
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }
}