package com.example.gpacalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SemesterDetailsActivity extends AppCompatActivity {
    List<String> mLectures = new ArrayList<String>();
    List<String> mECTSes = new ArrayList<String>();
    List<String> mMidterms = new ArrayList<String>();
    List<String> mFinals = new ArrayList<String>();
    List<String> mMakeUps = new ArrayList<String>();
    List<String> mAverages = new ArrayList<String>();
    List<String> mLetterGrades = new ArrayList<String>();
    List<String> mStatuses = new ArrayList<String>();
    RequestQueue mque;
    List<String> lectureList = new ArrayList<String>();
    List<String> ectsList = new ArrayList<String>();
    SharedPreferences sharedPref;

    List<String> letterGrades = new ArrayList<String>();
    List<String> coefficients = new ArrayList<String>();
    List<String> startPoints = new ArrayList<String>();
    List<String> finishPoints = new ArrayList<String>();
    List<String> status = new ArrayList<String>();
    String semestername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_details);
        mque = Volley.newRequestQueue(this);
        Spinner spinner=findViewById(R.id.spinner);

        Intent intent = getIntent();
        semestername = intent.getStringExtra("semester");
        String[] typeoflettergrades = new String[]{"KGTU 2022 (A,A-,B+,B...)", "KGTU 2015-2022 (AA,BA,BB,CB,CC...)"};
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),
                R.layout.spinner_item,typeoflettergrades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    letterGrades.clear();
                    coefficients.clear();
                    startPoints.clear();
                    finishPoints.clear();
                    status.clear();
                    GetLectureLetterGrade("grades");
                }
                if (i == 1) {
                    letterGrades.clear();
                    coefficients.clear();
                    startPoints.clear();
                    finishPoints.clear();
                    status.clear();

                    GetLectureLetterGrade("gradesold");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {

            SharedPreferences sha = getSharedPreferences(semestername, MODE_APPEND);

            JSONObject json= new JSONObject(sha.getString("name", ""));
            spinner.setSelection(json.getInt("GradeType"));
            try {
                JSONArray contacts = json.getJSONArray("");

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    Lectures lectures = new Lectures(c.getString("name"), Double.parseDouble(c.getString("ects")), Double.parseDouble(c.getString("midtermg")), Double.parseDouble(c.getString("finalg")), Double.parseDouble(c.getString("makeupg")), Double.parseDouble(c.getString("average")), c.getString("lettergrade"), c.getString("status"));
                    mLectures.add(lectures.getName());
                    mECTSes.add(String.valueOf(lectures.getEcts()));
                    mMidterms.add(String.valueOf(lectures.getMidtermg()));
                    mFinals.add(String.valueOf(lectures.getFinalg()));
                    mMakeUps.add(String.valueOf(lectures.getMakeupg()));
                    mAverages.add(String.valueOf(lectures.getAverage()));
                    mLetterGrades.add(String.valueOf(lectures.getLetterGrade()));
                    mStatuses.add(String.valueOf(lectures.getStatus()));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e("Erorr",e.getMessage());
        }
        SetAdapter();


            GetLectures();
        try {
            CalculateSemesterAverage();
        }catch (Exception e)
        {Log.e("Error",e.getMessage());}
        Button btn = findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lectures lectures = new Lectures("", 0, 0, 0, 0, 0, "F", "Fail");
                mLectures.add(lectures.getName());
                mECTSes.add(String.valueOf(lectures.getEcts()));
                mMidterms.add(String.valueOf(lectures.getMidtermg()));
                mFinals.add(String.valueOf(lectures.getFinalg()));
                mMakeUps.add(String.valueOf(lectures.getMakeupg()));
                mAverages.add(String.valueOf(lectures.getAverage()));
                mLetterGrades.add(String.valueOf(lectures.getLetterGrade()));
                mStatuses.add(String.valueOf(lectures.getStatus()));

                SetAdapter();

            }
        });
        // makeright();
    }


    class SemesterAdapter extends ArrayAdapter {

        Context context;
        List<String> rLectures = new ArrayList<String>();
        List<String> rECTSes = new ArrayList<String>();
        List<String> rMidterms = new ArrayList<String>();
        List<String> rFinals = new ArrayList<String>();
        List<String> rMakeUps = new ArrayList<String>();
        List<String> rAverages = new ArrayList<String>();
        List<String> rLetterGrades = new ArrayList<String>();
        List<String> rStatuses = new ArrayList<String>();


        SemesterAdapter(Context c, List<String> lectures, List<String> ectses, List<String> midterms, List<String> finals, List<String> makeups, List<String> averages, List<String> lettergrades, List<String> statuses) {

            super(c, R.layout.custom_semester_details_lectures, R.id.homteamtext, lectures);
            this.context = c;
            this.rLectures = lectures;
            this.rECTSes = ectses;
            this.rMidterms = midterms;
            this.rFinals = finals;
            this.rMakeUps = makeups;
            this.rAverages = averages;
            this.rLetterGrades = lettergrades;
            this.rStatuses = statuses;

        }


        @SuppressLint("ViewHolder")
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @NonNull
        @Override
        public View getView(int position, @Nullable View row, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.custom_semester_details_lectures, parent, false);

            AutoCompleteTextView lecture = row.findViewById(R.id.autocompleted);
            Button btn=row.findViewById(R.id.remove);
            EditText ects = row.findViewById(R.id.edittext1);
            EditText midterm = row.findViewById(R.id.edittext2);
            EditText finals = row.findViewById(R.id.edittext3);
            EditText makeup = row.findViewById(R.id.edittext4);
            TextView average = row.findViewById(R.id.textviewave);
            TextView lettergrade = row.findViewById(R.id.textviewlet);
            TextView status = row.findViewById(R.id.textview4);
            RelativeLayout rlt = row.findViewById(R.id.relative1);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLectures.remove(position);
                    mECTSes.remove(position);
                    mMidterms.remove(position);
                    mFinals.remove(position);
                    mMakeUps.remove(position);
                    mAverages.remove(position);
                    mLetterGrades.remove(position);
                    mStatuses.remove(position);
                    SetAdapter();
                    //SetDetails(average, midterm, finals, makeup, rAverages, position, lettergrade, rLetterGrades, status, rStatuses);

                }
            });
            lecture.setText(rLectures.get(position));
            ects.setText(rECTSes.get(position));
            midterm.setText(rMidterms.get(position));
            finals.setText(rFinals.get(position));
            makeup.setText(rMakeUps.get(position));
            average.setText(rAverages.get(position));
            lettergrade.setText(rLetterGrades.get(position));
            status.setText(rStatuses.get(position));

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_spinner_item, lectureList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            lecture.setAdapter(adapter);
            ColourRows(rlt, status, average);

            if(letterGrades.size()>0) {
                SetDetails(average, midterm, finals, makeup, rAverages, position, lettergrade, rLetterGrades, status, rStatuses);

               try{ CalculateSemesterAverage();}catch (Exception e){}
            }

            lecture.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    rLectures.set(position, String.valueOf(lecture.getText().toString()));
                    mLectures.set(position, String.valueOf(lecture.getText().toString()));

                    for (int a = 0; a < lectureList.size(); a++) {
                        if (lectureList.get(a).contentEquals(lecture.getText())) {
                            ects.setText(ectsList.get(a));
                            rECTSes.set(position, String.valueOf(ects.getText()));
                            mECTSes.set(position, String.valueOf(ects.getText()));
                        }
                    }
                    SetDetails(average, midterm, finals, makeup, rAverages, position, lettergrade, rLetterGrades, status, rStatuses);
                    ColourRows(rlt, status, average);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            ects.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    rECTSes.set(position, String.valueOf(ects.getText()));
                    mECTSes.set(position, String.valueOf(ects.getText()));

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            midterm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (ControlGrade(midterm))
                        midterm.setText("" + 100.0);
                    rMidterms.set(position, String.valueOf(midterm.getText()));
                    mMidterms.set(position, String.valueOf(midterm.getText()));
                    SetDetails(average, midterm, finals, makeup, rAverages, position, lettergrade, rLetterGrades, status, rStatuses);
                    ColourRows(rlt, status, average);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            finals.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (ControlGrade(finals))
                        finals.setText("" + 100.0);
                    rFinals.set(position, String.valueOf(finals.getText()));
                    mFinals.set(position, String.valueOf(finals.getText()));
                    SetDetails(average, midterm, finals, makeup, rAverages, position, lettergrade, rLetterGrades, status, rStatuses);
                    ColourRows(rlt, status, average);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            makeup.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (ControlGrade(makeup))
                        makeup.setText("" + 100.0);
                    rMakeUps.set(position, String.valueOf(makeup.getText()));
                    mMakeUps.set(position, String.valueOf(makeup.getText()));
                    SetDetails(average, midterm, finals, makeup, rAverages, position, lettergrade, rLetterGrades, status, rStatuses);
                    ColourRows(rlt, status, average);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            lettergrade.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    rLetterGrades.set(position, String.valueOf(lettergrade.getText()));
                    mLetterGrades.set(position, String.valueOf(lettergrade.getText()));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            status.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    rStatuses.set(position, String.valueOf(status.getText()));
                    mStatuses.set(position, String.valueOf(status.getText()));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return row;


        }
    }

    public void SetAdapter() {
        ListView listView = findViewById(R.id.listview);
        SemesterAdapter adapter = new SemesterAdapter(this, mLectures, mECTSes, mMidterms, mFinals, mMakeUps, mAverages, mLetterGrades, mStatuses);
        listView.setAdapter(adapter);
    }

    public void GetLectures() {
        String URL = "https://raw.githubusercontent.com/R-Fatih/CENG2003FinalProject/main/lectures.json";
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contacts = response.getJSONArray("Lectures");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                Log.e("a", c.getString("ECTS"));
                                lectureList.add(c.getString("Lname"));
                                ectsList.add(c.getString("ECTS"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());

                    }
                }
        );
        mque.add(objectRequest);


    }

    public void GetLectureLetterGrade(String type) {
        String URL = "https://raw.githubusercontent.com/R-Fatih/CENG2003FinalProject/main/" + type + ".json";
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contacts = response.getJSONArray("Grades");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                letterGrades.add(c.getString("Letter Grade"));
                                coefficients.add(c.getString("Coefficient"));
                                startPoints.add(c.getString("Start Point"));
                                finishPoints.add(c.getString("Finish Point"));
                                status.add(c.getString("Status"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());

                    }
                }
        );
        mque.add(objectRequest);


    }

    public double CalculateAverage(double mt, double finals, double mu) {

        return mu == 0.0 ? Double.parseDouble(String.valueOf((mt * 0.4) + (finals * 0.6))) : Double.parseDouble(String.valueOf((mt * 0.4) + (mu * 0.6)));
    }

    public String CalculateLetterGrade(double average) {
        String let = "";
        for (int i = 0; i < startPoints.size(); i++) {
            if (Math.round(average) <= Double.parseDouble(finishPoints.get(i)) && Math.round(average) >= Double.parseDouble(startPoints.get(i))) {
                let = letterGrades.get(i);
            }
        }
        return let;
    }

    public String CalculateStatus(String letterGrade) {
        String stat = "";
        for (int i = 0; i < startPoints.size(); i++) {
            if (letterGrades.get(i).equals(letterGrade)) {
                stat = status.get(i);
            }
        }
        return stat;

    }

    public String CalculateCoefficient(String letterGrade) {
        String coef = "";
        for (int i = 0; i < startPoints.size(); i++) {
            if (letterGrades.get(i).equals(letterGrade)) {
                coef = coefficients.get(i);
            }
        }
        return coef;

    }

    public void SetDetails(TextView average, EditText midterm, EditText finals, EditText makeup, List<String> rAverages, int position, TextView lettergrade, List<String> rLetterGrades, TextView status, List<String> rStatuses) {
        Log.e("Avr", String.valueOf(CalculateAverage(Double.valueOf(midterm.getText().toString()), Double.valueOf(finals.getText().toString()), Double.valueOf(makeup.getText().toString()))));
        average.setText( String.valueOf(CalculateAverage(Double.valueOf(midterm.getText().toString()), Double.valueOf(finals.getText().toString()), Double.valueOf(makeup.getText().toString()))));
        rAverages.set(position, String.valueOf(average.getText()));
        mAverages.set(position, String.valueOf(average.getText()));
        lettergrade.setText(CalculateLetterGrade(Double.parseDouble(average.getText().toString())));
        rLetterGrades.set(position, String.valueOf(lettergrade.getText()));
        mLetterGrades.set(position, String.valueOf(lettergrade.getText()));
        status.setText(CalculateStatus(lettergrade.getText().toString()));
        rStatuses.set(position, String.valueOf(status.getText()));
        mStatuses.set(position, String.valueOf(status.getText()));
try {
    CalculateSemesterAverage();
}catch (Exception e){}
              JsonWriter jsonWriter = new JsonWriter();
        JSONObject json = new JSONObject();
        Spinner spinner=findViewById(R.id.spinner);
        TextView textView = findViewById(R.id.semesteraverage);

        try {
            json.put("GradeType",spinner.getSelectedItemPosition());
            json.put("SemesterAverage",textView.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray();
        for (int i = 0; i < mLectures.size(); i++) {
            jsonWriter.createJson(json, array, mLectures, mECTSes, mMidterms, mFinals, mMakeUps, mAverages, mLetterGrades, mStatuses, i);

        }


        Log.e("json", json.toString());
        SharedPreferences sharedPreferences = getSharedPreferences(semestername, MODE_PRIVATE);

        SharedPreferences.Editor shr = sharedPreferences.edit();
        shr.putString("name", json.toString());
        shr.commit();

    }

    public void ColourRows(RelativeLayout rlt, TextView status, TextView average) {
        if (status.getText().toString().equals("Pass") && Double.parseDouble(average.getText().toString()) != 0.0)
            rlt.setBackgroundColor(Color.rgb(0, 188, 212));
        else if (status.getText().toString().equals("Conditional Pass") && Double.parseDouble(average.getText().toString()) != 0.0)
            rlt.setBackgroundColor(Color.rgb(255, 193, 7));
        else if (status.getText().toString().equals("Fail") && Double.parseDouble(average.getText().toString()) != 0.0)
            rlt.setBackgroundColor(Color.rgb(233, 30, 99));
    }

    public boolean ControlGrade(EditText editText) {
        return (Double.parseDouble(editText.getText().toString()) > 100.0);
    }
    public void CalculateSemesterAverage(){
        double totalects = 0;
        double coeff = 0;
        for (int i = 0; i < mLectures.size(); i++) {
            totalects += Double.parseDouble(mECTSes.get(i));
            coeff += Double.parseDouble(mECTSes.get(i)) * Double.parseDouble(CalculateCoefficient(mLetterGrades.get(i)));
        }
        TextView textView = findViewById(R.id.semesteraverage);
        textView.setText("" + new DecimalFormat("##.##").format((coeff / totalects)));

    }
}