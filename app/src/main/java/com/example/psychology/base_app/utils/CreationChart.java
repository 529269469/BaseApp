package com.example.psychology.base_app.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 简单封装一下
 */
public class CreationChart {
    private LineChart chart;
    private List<String> dateTime = new ArrayList<>();

    public CreationChart(LineChart chart) {
        this.chart = chart;
    }

    /***
     * 初始化
     */
    public void init() {
        // 开启文本描述
        chart.getDescription().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.9f);
        // 开启触摸手势
        chart.setTouchEnabled(true);
        // 允许缩放和拖动
        chart.setDragEnabled(true); //拖动
        chart.setScaleEnabled(true); //缩放
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setHighlightPerDragEnabled(true);
        chart.setDoubleTapToZoomEnabled(false);


        // 如果禁用，可以分别在x轴和y轴上进行缩放
        chart.setPinchZoom(false);

        // 设置一个替代背景
        //chart.setBackgroundColor(Color.rgb(255, 255, 255));
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);//标签位置
        xl.setTextColor(Color.GRAY); // x值为白色
        xl.setDrawGridLines(false);
        xl.setLabelCount(6); //分为几个
        xl.setAxisLineColor(Color.rgb(221, 218, 218)); //x线的颜色
        xl.setAvoidFirstLastClipping(true);

        xl.setEnabled(true);

        //左边的Y轴数据
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        //右边的Y轴数据
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(Color.GRAY);
        rightAxis.setAxisMinimum(0f);//最小条目
        rightAxis.setAxisMaximum(100f);
        rightAxis.setLabelCount(6);//设置最大分为几格
        rightAxis.setGridColor(Color.rgb(221, 218, 218));
        rightAxis.setDrawGridLines(true);
        rightAxis.setAxisLineColor(Color.rgb(221, 218, 218));
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(true);
    }

    public void setMax(int max) {
        chart.setVisibleXRange(0, max);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    int time = 0;

    public void AddData(float... data) {
        //获取当前时间  格式为“HH:mm:ss”

        time++;
        dateTime.add(time + "s");
        LineDataSet newDataSet, newDataSet2, newDataSet3, newDataSet4, newDataSet5, newDataSet6, newDataSet7, newDataSet8;
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateTime));

        switch (data.length) {
            // 单折线
            case 1:
                if (chart.getData() != null &&
                        chart.getData().getDataSetCount() > 0) {
                    newDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    newDataSet.addEntry(new Entry(newDataSet.getEntryCount(), data[0]));
                } else {
                    newDataSet = CreateSet(LineChartType.CHART1);
                    LineData lineData1 = new LineData(newDataSet);
                    chart.setData(lineData1);
                }
                break;
            // 多折线
            case 2:
                if (chart.getData() != null &&
                        chart.getData().getDataSetCount() > 0) {
                    newDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    newDataSet2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
                    newDataSet.addEntry(new Entry(newDataSet.getEntryCount(), data[0]));
                    newDataSet2.addEntry(new Entry(newDataSet2.getEntryCount(), data[1]));
                } else {
                    newDataSet = CreateSet(LineChartType.CHART1);
                    newDataSet2 = CreateSet(LineChartType.CHART2);
                    LineData lineData1 = new LineData(newDataSet, newDataSet2);
                    chart.setData(lineData1);
                }
                break;
            case 8:
                if (chart.getData() != null &&
                        chart.getData().getDataSetCount() > 0) {
                    newDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    newDataSet2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
                    newDataSet3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
                    newDataSet4 = (LineDataSet) chart.getData().getDataSetByIndex(3);
                    newDataSet5 = (LineDataSet) chart.getData().getDataSetByIndex(4);
                    newDataSet6 = (LineDataSet) chart.getData().getDataSetByIndex(5);
                    newDataSet7 = (LineDataSet) chart.getData().getDataSetByIndex(6);
                    newDataSet8 = (LineDataSet) chart.getData().getDataSetByIndex(7);
                    newDataSet.addEntry(new Entry(newDataSet.getEntryCount(), data[0]));
                    newDataSet2.addEntry(new Entry(newDataSet2.getEntryCount(), data[1]));
                    newDataSet3.addEntry(new Entry(newDataSet3.getEntryCount(), data[2]));
                    newDataSet4.addEntry(new Entry(newDataSet4.getEntryCount(), data[3]));
                    newDataSet5.addEntry(new Entry(newDataSet5.getEntryCount(), data[4]));
                    newDataSet6.addEntry(new Entry(newDataSet6.getEntryCount(), data[5]));
                    newDataSet7.addEntry(new Entry(newDataSet7.getEntryCount(), data[6]));
                    newDataSet8.addEntry(new Entry(newDataSet8.getEntryCount(), data[7]));
                } else {
                    newDataSet = CreateSet(LineChartType.CHART1);
                    newDataSet2 = CreateSet(LineChartType.CHART2);
                    newDataSet3 = CreateSet(LineChartType.CHART3);
                    newDataSet4 = CreateSet(LineChartType.CHART4);
                    newDataSet5 = CreateSet(LineChartType.CHART5);
                    newDataSet6 = CreateSet(LineChartType.CHART6);
                    newDataSet7 = CreateSet(LineChartType.CHART7);
                    newDataSet8 = CreateSet(LineChartType.CHART8);
                    LineData lineData1 = new LineData(newDataSet, newDataSet2, newDataSet3, newDataSet4, newDataSet5, newDataSet6, newDataSet7, newDataSet8);
                    chart.setData(lineData1);
                }
                break;
        }
        chart.getData().notifyDataChanged();
        //提交数据更新
        chart.notifyDataSetChanged();
        //设置X最大可见条目
        chart.setVisibleXRange(0, 6);
        //移动到最新数据
        if (chart.getData().getDataSetByIndex(0).getEntryCount() > 0)
            chart.moveViewToX(chart.getData().getDataSetByIndex(0).getEntryCount());
    }


    /**
     * 设置数据格式
     *
     * @param type chart1 或者 chart2
     * @return LineDataSet
     */
    private LineDataSet CreateSet(LineChartType type) {
        LineDataSet set = null;
        switch (type) {
            case CHART1:
                set = new LineDataSet(null, "专注");
                set.setColor(Color.rgb(197, 139, 242)); //折线颜色
                set.setFillColor(ColorTemplate.getHoloBlue());
                break;
            case CHART2:
                set = new LineDataSet(null, "放松");
                set.setColor(Color.rgb(146, 163, 253)); //折线颜色
                set.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
                break;
            case CHART3:
            case CHART4:
            case CHART5:
            case CHART6:
            case CHART7:
            case CHART8:
                set = new LineDataSet(null, "放松");
                set.setColor(Color.rgb(146, 163, 200)); //折线颜色
                set.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
                break;

        }
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setHighLightColor(Color.rgb(124, 117, 117));//高亮颜色
        set.setDrawValues(false); //绘画值
        set.setDrawCircles(false); //绘画圆圈
        set.setDrawFilled(false); //充满底部
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);  // 类型，折线还是曲线还是 平线
        return set;
    }

    /**
     * 枚举类型
     */
    private enum LineChartType {
        CHART1, CHART2, CHART3, CHART4, CHART5, CHART6, CHART7, CHART8
    }
}

