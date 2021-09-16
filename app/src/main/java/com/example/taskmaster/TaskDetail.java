package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent= getIntent();

        String title=intent.getExtras().getString("title");
        String disc=intent.getExtras().getString("body");
        String state=intent.getExtras().getString("state");
        TextView titlePage=findViewById(R.id.titileID);
        TextView discPage=findViewById(R.id.descriptionId);
        TextView statePage=findViewById(R.id.stateDetailID);

        titlePage.setText(title);
        discPage.setText(disc);
        statePage.setText(state);


        Amplify.Storage.downloadFile(
                intent.getExtras().getString("imgName"),
                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
                result -> {
                    ImageView imageView = findViewById(R.id.imageView);
                    String newImg = result.getFile().getPath();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(newImg));

                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile());},
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );
    }
}