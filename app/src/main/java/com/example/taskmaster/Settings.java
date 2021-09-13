package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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


        Button save=findViewById(R.id.saveId);
        save.setOnClickListener((view)->{
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Settings.this);
            SharedPreferences.Editor  sharedPreferencesEditor = sharedPreferences.edit();
            EditText nameField=findViewById(R.id.userNameId);
            String name=nameField.getText().toString();
            sharedPreferencesEditor.putString("username",name);


            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            int chosenButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton chosenButton = findViewById(chosenButtonId);
            String teamName = chosenButton.getText().toString();
            sharedPreferencesEditor.putString("teamName", teamName);
            sharedPreferencesEditor.putString("teamId", teamsList.get(teamName));

            sharedPreferencesEditor.apply();

            Intent goMain=new Intent(Settings.this,MainActivity.class);
            startActivity(goMain);

        });

    }
}