package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button save=findViewById(R.id.saveId);
        save.setOnClickListener((view)->{
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Settings.this);
            SharedPreferences.Editor  sharedPreferencesEditor = sharedPreferences.edit();
            EditText nameField=findViewById(R.id.userNameId);
            String name=nameField.getText().toString();
            sharedPreferencesEditor.putString("username",name);
            sharedPreferencesEditor.apply();
            Intent goMain=new Intent(Settings.this,MainActivity.class);
            startActivity(goMain);

        });

    }
}