package com.organizationiworkfor.stormy.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.organizationiworkfor.stormy.R;
import com.organizationiworkfor.stormy.weatherData.Hour;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GraphingActivity extends AppCompatActivity {
    private Hour[] mHours;
    @Bind(R.id.timeTextView) TextView mTime;
    @Bind(R.id.sumTextView) TextView mSum;
    @Bind(R.id.iconImageView) ImageView mImage;
    @Bind(R.id.tempTextView) TextView mTemp;
    @Bind(R.id.humValue) TextView mHum;
    @Bind(R.id.precipValue) TextView mPrecip;
    @Bind(R.id.chart) BarChart mChart;
    @Bind(R.id.degTextView) TextView mDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setTouchEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextSize(12f); // set the textsize
        yAxis.setAxisMaxValue(100f); // the axis maximum is 100
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        ArrayList<String> chartDataX = new ArrayList<String>();
        ArrayList<BarEntry> chartDataY = new ArrayList<BarEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int hiTemp = 0;
        int loTemp = 200;
        int num = 0;
        for (int i = 0; i < 24; i++) {
            Hour hourData = mHours[i];
            chartDataX.add(hourData.getHour());
            BarEntry entry = new BarEntry(hourData.getTemperature(), i);
            chartDataY.add(entry);
            if (hiTemp < hourData.getTemperature()) {
                hiTemp = hourData.getTemperature();
            }
            if (loTemp > hourData.getTemperature()) {
                loTemp = hourData.getTemperature();
            }
            if (hourData.getHour().equals("6 PM")) {
                num = i;
            }
        }

        for (int i = 0; i < 24; i++) {
            if (i == num) {
                for (int i2 = 0; i2 < 12; i2++) {
                    colors.add(Color.GRAY);
                }
            } else {
                colors.add(Color.WHITE);
            }
        }

        yAxis.setAxisMaxValue(hiTemp + 2);
        yAxis.setAxisMinValue(loTemp - 2);

        BarDataSet set1 = new BarDataSet(chartDataY, "Dataset");
        set1.setColors(colors);
        BarData data = new BarData(chartDataX, set1);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.animateY(1500);
        mChart.setVisibleXRangeMaximum(18);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Hour hourData = mHours[h.getXIndex()];
                mTime.setText("At " + hourData.getHour() + " it will be");
                mTemp.setText(hourData.getTemperature()+"");
                mSum.setText(hourData.getSummary());
                mHum.setText(hourData.getHumidity() + "");
                mPrecip.setText(hourData.getPrecip());
                Drawable drawable = getResources().getDrawable(hourData.getIconId());
                mImage.setImageDrawable(drawable);
                mDegree.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}