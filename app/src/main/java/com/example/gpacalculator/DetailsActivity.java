package com.example.gpacalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        try {

            SharedPreferences sha = getSharedPreferences("1sem", MODE_APPEND);
            JSONObject json= new JSONObject(sha.getString("name", ""));
            SharedPreferences sha2 = getSharedPreferences("2sem", MODE_APPEND);
            JSONObject json2= new JSONObject(sha2.getString("name", ""));
            SharedPreferences sha3 = getSharedPreferences("3sem", MODE_APPEND);
            JSONObject json3= new JSONObject(sha3.getString("name", ""));
            SharedPreferences sha4 = getSharedPreferences("4sem", MODE_APPEND);
            JSONObject json4= new JSONObject(sha4.getString("name", ""));
            SharedPreferences sha5 = getSharedPreferences("5sem", MODE_APPEND);
            JSONObject json5= new JSONObject(sha5.getString("name", ""));
            SharedPreferences sha6 = getSharedPreferences("6sem", MODE_APPEND);
            JSONObject json6= new JSONObject(sha6.getString("name", ""));
            SharedPreferences sha7 = getSharedPreferences("7sem", MODE_APPEND);
            JSONObject json7= new JSONObject(sha7.getString("name", ""));
            SharedPreferences sha8 = getSharedPreferences("8sem", MODE_APPEND);
            JSONObject json8= new JSONObject(sha8.getString("name", ""));

        GraphView graph = (GraphView) findViewById(R.id.graphview);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, json.getDouble("SemesterAverage")),
                new DataPoint(2, json2.getDouble("SemesterAverage")),
                new DataPoint(3, json3.getDouble("SemesterAverage")),
                new DataPoint(4, json4.getDouble("SemesterAverage")),
                new DataPoint(5, json5.getDouble("SemesterAverage")),
                new DataPoint(6, json6.getDouble("SemesterAverage")),
                new DataPoint(7, json7.getDouble("SemesterAverage")),
                new DataPoint(8, json8.getDouble("SemesterAverage"))
        });
        graph.addSeries(series);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}