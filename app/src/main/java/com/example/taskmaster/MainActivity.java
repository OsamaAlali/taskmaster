package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTaskButton=findViewById(R.id.addTaskId);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goAddTaskPage=new Intent(MainActivity.this,AddTask.class);
                startActivity(goAddTaskPage);
            }});

        Button allTaskButon=findViewById(R.id.allTaskId);
        allTaskButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goAllTaskPage= new Intent(MainActivity.this, AllTasks.class);
                startActivity(goAllTaskPage);
            }
        });

    }
}