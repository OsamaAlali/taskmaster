package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.datastore.generated.model.Todo;

import java.util.HashMap;
import java.util.Map;

public class AddTask extends AppCompatActivity {
    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);
        Button submitButton=findViewById(R.id.save);

        EditText titleGet=findViewById(R.id.titileTaskId);
        String title=titleGet.getText().toString();

        EditText discGet=findViewById(R.id.descrptionTaskId);
        String disc=discGet.getText().toString();

        EditText statecGet=findViewById(R.id.stateId);
        String state=statecGet.getText().toString();


        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        Map< String,String> teamsList = new HashMap<>();
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Team.class),
                response -> {
                    for (Team oneTeam : response.getData()) {
                        teamsList.put(oneTeam.getName(), oneTeam.getId());
                    }
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int chosenButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton chosenButton = findViewById(chosenButtonId);
                String chosenTeam = chosenButton.getText().toString();

                Amplify.API.query(
                        ModelQuery.get(Team.class, teamsList.get(chosenTeam)),
                        response -> {
                            Log.i("MyAmplifyApp", ((Team) response.getData()).getName());

                           Todo todo = Todo.builder()
                                    .title(title)
                                    .body(disc)
                                    .state(state)
                                    .build();

                            Amplify.API.mutate(
                                    ModelMutation.create(todo),
                                    response2 -> Log.i("MyAmplifyApp", "Added Todo with id: " + response2.getData().getId()),
                                    error -> Log.e("MyAmplifyApp", "Create failed", error)
                            );
                        },
                        error -> Log.e("MyAmplifyApp", error.toString(), error)
                );


////        inset into database
//                Task task1=new Task(title,disc,state);
//                    Todo todo = Todo.builder()
//                            .title(title)
//                            .body(disc)
//                            .state(state)
//                            .build();
//                    Amplify.API.mutate(
//                            ModelMutation.create(todo),
//                            response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                            error -> Log.e("MyAmplifyApp", "Create failed", error)
//                    );

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