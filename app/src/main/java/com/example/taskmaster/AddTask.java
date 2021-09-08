package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;

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
////        inset into database

//                Task task1=new Task(title,disc,state);




                    Todo todo = Todo.builder()
                            .title(title)
                            .body(disc)
                            .state(state)
                            .build();

                    Amplify.API.mutate(
                            ModelMutation.create(todo),
                            response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                            error -> Log.e("MyAmplifyApp", "Create failed", error)
                    );





//                appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"osamaDatabase").allowMainThreadQueries().build();
//
//                TaskDao taskDao=appDatabase.taskDao();
//
//                taskDao.insertAll(task1);


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