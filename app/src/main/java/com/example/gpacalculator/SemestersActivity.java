package com.example.gpacalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SemestersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesters);
        ListView listview=findViewById(R.id.listview);
        String[] semesters=new String[]{"1st Semester","2nd Semester","3rd semester","4th Semester","5th Semester","6th Semester","7th Semester","8th Semester"};
       ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),
                R.layout.list_item,R.id.text_view, semesters);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

listview.setAdapter(adapter);
listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(SemestersActivity.this,SemesterDetailsActivity.class);
        intent.putExtra("semester",(i+1+"sem"));
        startActivity(intent);
    }
});
    }
}