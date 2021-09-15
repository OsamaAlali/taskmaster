package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.datastore.generated.model.Todo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
     AppDatabase appDatabase; // object
//    RecyclerView allTasksRecuclerView;

    public static final String TAG = MainActivity.class.getSimpleName();

    private static PinpointManager pinpointManager;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", String.valueOf(userStateDetails.getUserState()));
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }



        Amplify.Auth.signInWithWebUI(
                MainActivity.this,
                result -> Log.i("AuthQuickStart", result.toString()),
                error -> Log.e("AuthQuickStart", error.toString())
        );

        Button logOutBtn=findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        AuthSignOutOptions.builder().globalSignOut(true).build(),
                        () -> {
                            Log.i("AuthQuickstart", "Signed out globally");
                            finish();
                            startActivity(getIntent());
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );


            }
        });

//
//
//            TextView text1=findViewById(R.id.welcom);
//            text1.setText(Amplify.Auth.getCurrentUser().getUsername());
//
//        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamId = sharedPreferences.getString("teamId", "");

//        Team team1 = Team.builder().name("team1").build();
//        Team team2 = Team.builder().name("team2").build();
//        Team team3 = Team.builder().name("team3").build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "osaDatabase").allowMainThreadQueries().build();
        if (!teamId.equals("")) {
            RecyclerView allTasksRecuclerView = findViewById(R.id.taskViewID);
            Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    allTasksRecuclerView.getAdapter().notifyDataSetChanged();
                    return false;
                }
            });
            List<Todo> taskList = new ArrayList<>();
            Amplify.API.query(
                    ModelQuery.get(Team.class ,teamId),
                    response -> {
                        if(response.getData() != null)
                        for (Todo task : response.getData().getTodo()) {
                            Log.i("MyAmplifyApp", task.getTitle());
                            Log.i("MyAmplifyApp", task.getBody());
                            Log.i("MyAmplifyApp", task.getState());
                            taskList.add(task);
                        }
                        handler.sendEmptyMessage(1);
                    },
                    error -> Log.e("MyAmplifyApp", "Query failure", error)
            );
            allTasksRecuclerView.setLayoutManager(new LinearLayoutManager(this));
            allTasksRecuclerView.setAdapter(new TaskAdabter(taskList));
        }
//
        Button addTaskBtn=findViewById(R.id.addTaskBtn);
addTaskBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,AddTask.class);
        startActivity(intent);
    }
});

              // to read from data base
//        appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"osamaDatabase").allowMainThreadQueries().build();

//    List<Task> taskList = appDatabase.taskDao().getAll();

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
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onStart() {
        super.onStart();
        String endName="'s tasks Team: ";
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String name=sharedPreferences.getString("username","user");
        String chooseTeamName= sharedPreferences.getString("teamName", "No team");
        TextView viewName=findViewById(R.id.userNameHomeId);
        viewName.setText(name+endName+chooseTeamName);
    }


}