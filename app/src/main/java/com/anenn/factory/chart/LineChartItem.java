
package com.anenn.factory.chart;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.anenn.factory.R;
import com.anenn.factory.chart.ChartItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

public class LineChartItem extends ChartItem {

//    private Typeface mTf;

    public LineChartItem(ChartData<?> cd, Context c) {
        super(cd);
//        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.chart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.setDescription("");
        holder.chart.setDrawGridBackground(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(34, 105, 213));
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = holder.chart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaxValue(100);
        leftAxis.setTextColor(Color.rgb(34, 105, 213));
        leftAxis.setDrawAxisLine(false);
        leftAxis.setLabelCount(5, false);

        YAxis rightAxis = holder.chart.getAxisRight();
//        rightAxis.setTypeface(mTf);
//        rightAxis.setAxisMaxValue(100);
//        rightAxis.setAxisMinValue(0);
//        leftAxis.setLabelCount(5, false);
//        rightAxis.setDrawLabels(true);
//        rightAxis.setDrawTopYLabelEntry(false); // 绘制Y轴上最上面的值
        rightAxis.setEnabled(false);
//        rightAxis.setTextColor(Color.rgb(34, 105, 213));
//        rightAxis.setDrawGridLines(false);

        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(750);
        holder.chart.setScaleEnabled(false);
        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }
}
