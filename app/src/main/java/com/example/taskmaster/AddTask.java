package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.datastore.generated.model.Todo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddTask extends AppCompatActivity {
    AppDatabase appDatabase;
    private FusedLocationProviderClient fusedLocationClient;
    String imgName="";

    public void fileChoose(){
        Intent fileChoose=new Intent(Intent.ACTION_GET_CONTENT);
        fileChoose.setType("*/*");
        fileChoose=Intent.createChooser(fileChoose,"choose file");
        startActivityForResult(fileChoose,22);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            InputStream exampleInputStream = getContentResolver().openInputStream(data.getData());

            imgName = data.getData().toString();


            Amplify.Storage.uploadInputStream(
                    data.getData().toString(),
                    exampleInputStream,
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        }  catch (FileNotFoundException error) {
            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1234);

            boolean x =ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat
                            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED;
            // here to request the missing permissions, and then overriding
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double longitude= location.getLongitude();
                            double latitude= location.getLatitude();
                            TextView tex1=findViewById(R.id.location);
                            tex1.setText(" longitude"  +longitude + " latitude "+ latitude );
                        }
                    }

                });

        setContentView(R.layout.activity_add_task);
        Button submitButton=findViewById(R.id.save);

        EditText titleGet=findViewById(R.id.titileTaskId);
        String title=titleGet.getText().toString();

        EditText discGet=findViewById(R.id.descrptionTaskId);
        String disc=discGet.getText().toString();

        EditText statecGet=findViewById(R.id.stateId);
        String state=statecGet.getText().toString();

        Intent intent = getIntent();
        if (intent.getType() != null && intent.getType().equals("text/plain")){
            EditText desc = findViewById(R.id.descrptionTaskId);
            desc.setText(intent.getExtras().get(Intent.EXTRA_TEXT).toString());
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
                                    .title(titleGet.getText().toString())
                                    .body(discGet.getText().toString())
                                    .state(statecGet.getText().toString())
                                   .taskTeam(response.getData())
                                    .build();

                            Amplify.API.mutate(
                                    ModelMutation.create(todo),
                                    response2 -> Log.i("MyAmplifyApp", "Added Todo with id: " + response2.getData().getId()),
                                    error -> Log.e("MyAmplifyApp", "Create failed", error)
                            );
                        },
                        error -> Log.e("MyAmplifyApp", error.toString(), error)
                );

               Intent goMain=new Intent(AddTask.this,MainActivity.class);
                startActivity(goMain);
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

        // buttom to uplode Image
        Button uploadImg = findViewById(R.id.uploadImg);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose();
            }
        });


    }// On Create




}