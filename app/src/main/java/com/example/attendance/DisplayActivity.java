package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashSet;

//To fetch data and add into view
public class DisplayActivity extends AppCompatActivity {

    //Declaration
    RecyclerView recyclerView;
    ArrayList<AttendancePojo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        recyclerView = findViewById(R.id.view);

        //Fetching data from DB
        DBHelper helper = new DBHelper(this,DBHelper.DATABASE_NAME,null,1);
        System.out.println("ID Modal is"+IDModal.getID());
        arrayList = helper.getStatus(IDModal.getID());//arraylist fetches an object to get name,date and id

        //Setting data in employee list view
        AttendanceAdaptor adp = new AttendanceAdaptor(DisplayActivity.this,arrayList);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}