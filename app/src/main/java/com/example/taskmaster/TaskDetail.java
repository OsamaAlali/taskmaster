package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent= getIntent();
        String title=intent.getExtras().getString("titile");
        String disc=intent.getExtras().getString("disc");
        TextView titlePage=findViewById(R.id.titileID);
        TextView discPage=findViewById(R.id.descriptionId);
        titlePage.setText(title);
        discPage.setText(disc);
    }
}