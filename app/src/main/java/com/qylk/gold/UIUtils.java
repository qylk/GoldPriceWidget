package com.qylk.gold;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UIUtils {

    public static Bitmap drawBitmap(Context context, List<Pair<Long, Float>> myData) {
        View root = LayoutInflater.from(context).inflate(R.layout.example_layout, null);
        LineChart lineChart = root.findViewById(R.id.chat);
        lineChart.setTouchEnabled(false);
        lineChart.setNoDataTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
//        lineChart.getXAxis().setLabelRotationAngle(-45);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴位置
        lineChart.getXAxis().setLabelCount(10);
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            DateFormat format = new SimpleDateFormat("MM-dd");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long va = (long) value;
                return format.format(new Date(va * 3600 * 1000 * 24));
            }
        });
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        Description description = lineChart.getDescription();
        description.setTextColor(Color.YELLOW);
        description.setTextSize(12);
        DateFormat format = new SimpleDateFormat("dd日HH点 更新");
        description.setText(format.format(new Date(System.currentTimeMillis())));
        List<Entry> entries = new ArrayList<>();
        for (Pair<Long, Float> item : myData) {
            entries.add(new Entry(item.first, item.second));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "value");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(9);
        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(context.getResources().getDisplayMetrics().widthPixels,
                View.MeasureSpec.EXACTLY);
        root.measure(measuredWidth, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        root.layout((int) root.getX(), (int) root.getY(), root.getMeasuredWidth(), root.getMeasuredHeight());
        if (root.getMeasuredHeight() <= 0 || root.getMeasuredWidth() <= 0) {
            return null;
        }
        Bitmap b = Bitmap.createBitmap(root.getMeasuredWidth(), root.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        b.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        Canvas c = new Canvas(b);
        root.draw(c);
        return b;
    }
}
