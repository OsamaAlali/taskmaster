package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




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
    protected void onRestart() {
        super.onRestart();
        String endName="'s tasks";
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String name=sharedPreferences.getString("username","user");
        TextView viewName=findViewById(R.id.userNameHomeId);
        viewName.setText(name+endName);
    }
}