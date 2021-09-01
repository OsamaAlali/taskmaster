package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button submitButton=findViewById(R.id.save);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        AppDatabase appDatabase;
                EditText titleGet=findViewById(R.id.titileTaskId);
                String title=titleGet.getText().toString();

                EditText discGet=findViewById(R.id.descrptionTaskId);
                String disc=discGet.getText().toString();

                EditText statecGet=findViewById(R.id.stateId);
                String state=statecGet.getText().toString();

                Task task1=new Task(title,disc,state);
                appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"osamaDatabase").allowMainThreadQueries().build();

                TaskDao taskDao=appDatabase.taskDao();

                taskDao.insertAll(task1);


            }
        });

        Button backMain=findViewById(R.id.backToMain);
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMain=new Intent(AddTask.this,MainActivity.class);
                startActivity(goMain);

            }
        });
    }

}