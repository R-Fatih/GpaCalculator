package com.example.gpacalculator;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class JsonWriter extends AppCompatActivity {
    public void createJson(JSONObject json,JSONArray array, List<String> lectures, List<String> ectses, List<String> midterms, List<String> finals, List<String> makeups, List<String> averages, List<String> lettergrades, List<String> statuses,int pos){
        JSONObject item = new JSONObject();

        try {
            item.put("name", lectures.get(pos));
            item.put("ects", ectses.get(pos));
            item.put("midtermg", midterms.get(pos));
            item.put("finalg", finals.get(pos));
            item.put("makeupg", makeups.get(pos));
            item.put("average", averages.get(pos));
            item.put("lettergrade", lettergrades.get(pos));
            item.put("status", statuses.get(pos));

            array.put(item);
            try {
                json.put("", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


}
