package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
     AppDatabase appDatabase; // object
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayList<Task> allTasks = new ArrayList<Task>();
//        allTasks.add(new Task("lab10","solve lab10 " ,"new"));
//        allTasks.add(new Task("lab18","solve lab18 " ,"assigned"));
//        allTasks.add(new Task("lab15","solve lab15 " ,"complete"));
//        allTasks.add(new Task("lab12","solve lab12 " ,"in progress"));

// Insert to database
//        Task task1=new Task("lab20","solve lab18 " ,"assigned");
//                appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"osamaDatabase").allowMainThreadQueries().build();
//
//              TaskDao taskDao=appDatabase.taskDao();
//
//              taskDao.insertAll(task1);

Button addTaskBtn=findViewById(R.id.addTaskBtn);
addTaskBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,AddTask.class);
        startActivity(intent);
    }
});

              // to read from data base
        appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"osamaDatabase").allowMainThreadQueries().build();

    List<Task> taskList = appDatabase.taskDao().getAll();

    RecyclerView allTasksRecuclerView = findViewById(R.id.taskViewID);
    allTasksRecuclerView.setLayoutManager(new LinearLayoutManager(this));
    allTasksRecuclerView.setAdapter(new TaskAdabter(taskList));




        


        Button lab10=findViewById(R.id.task1);

        lab10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title="Solve lab10";
                String disc="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries," +
                        " but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised";
                Intent goTaskDetail=new Intent(MainActivity.this,TaskDetail.class);
                goTaskDetail.putExtra("titile",title);
                goTaskDetail.putExtra("disc",disc);
                startActivity(goTaskDetail);
            }});

Button lab12=findViewById(R.id.task2);
 lab12.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String title="Solve lab12 Sort";
        String disc="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries," +
                " but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised";
        Intent goTaskDetail=new Intent(MainActivity.this,TaskDetail.class);
         goTaskDetail.putExtra("titile",title);
         goTaskDetail.putExtra("disc",disc);
         startActivity(goTaskDetail);
    }
});

 Button lab20=findViewById(R.id.task3);
 lab20.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         String title="Solve lab20 Treeeee";
         String disc="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries," +
                 " but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised";
         Intent goTaskDetaile=new Intent(MainActivity.this,TaskDetail.class);
         goTaskDetaile.putExtra("titile",title);
         goTaskDetaile.putExtra("disc",disc);

         startActivity(goTaskDetaile);
     }
 });

 Button setting=findViewById(R.id.settingsId);
 setting.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Intent goSettingsPage=new Intent(MainActivity.this,Settings.class);
         startActivity(goSettingsPage);
     }
 });


    }

    @Override
    protected void onStart() {
        super.onStart();
        String endName="'s tasks";
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String name=sharedPreferences.getString("username","user");
        TextView viewName=findViewById(R.id.userNameHomeId);
        viewName.setText(name+endName);
    }
}