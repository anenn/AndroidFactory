package com.anenn.factory.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.anenn.factory.R;
import com.anenn.factory.chart.DemoBase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * 饼图结构界面
 * Created by Anenn on 2015/12/10.
 */
public class PieCharActivity extends DemoBase implements OnChartValueSelectedListener {

    private PieChart mChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechar);

        initViewById();
        initViewValue();
    }

    private void initViewById() {
        mChart = (PieChart) findViewById(R.id.pieChart);
    }

    private void initViewValue() {
        mChart.setUsePercentValues(true); // 进行百分比显示
        mChart.setDescription("");
//        mChart.setExtraOffsets(5, 10, 5, 5); // 设置饼图间距偏移量

        mChart.setDragDecelerationFrictionCoef(0.95f); // 设置饼图拖动后离手后滚动的加速度

        mChart.setCenterText(generateCenterSpannableText()); // 设置饼图中心文本
//        mChart.setCenterTextColor(Color.parseColor("#00ff00"));
//        mChart.setCenterTextSize(10f);
//        mChart.setCenterTextRadiusPercent(0.5f); // 设置中心文本显示的长方形区域的角弧度

        mChart.setDrawSliceText(false); // 设置是否在扇形中显示x轴文本
        mChart.setDrawHoleEnabled(true); // 设置是否绘制中心圆
        mChart.setHoleColorTransparent(true); // 设置中心圆是否为透明，这个参数会影响中心文本的布局显示

        mChart.setTransparentCircleColor(Color.BLACK); // 内环颜色
        mChart.setTransparentCircleAlpha(0); // 内环透明度

        mChart.setHoleRadius(58f); // 中心圆的半径
        mChart.setTransparentCircleRadius(61f); // 中心外圆的半径，该值会决定内环的厚度

        mChart.setDrawCenterText(true); // 是否显示中心文本
        mChart.setLogEnabled(false); // 关闭日志

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true); // 开启/关闭旋转标志位
        mChart.setHighlightPerTapEnabled(true); // 设置点击扇形是否高亮显示

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this); // 扇形点击事件监听器

        setData(3, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad); // 动画
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(2f); // 设置扇形间的间距
        dataSet.setSelectionShift(5f);


        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        dataSet.setColors(colors);
//        dataSet.setSelectionShift(0f); // 设置扇形点击后的高亮半径

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

//        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("MPAndroidChart developed by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
}
