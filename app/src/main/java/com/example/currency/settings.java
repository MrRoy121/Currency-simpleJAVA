package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class settings extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch bg = findViewById(R.id.bg);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        boolean music = pref.getBoolean("musics",false);
        if (music) {
            bg.setChecked(true);
        } else {
            bg.setChecked(false);
        }
        bg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(settings.this, bgmusic.class));
                    bg.setChecked(true);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("musics", true);
                    editor.apply();
                } else{
                    stopService(new Intent(settings.this, bgmusic.class));
                    bg.setChecked(false);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("musics", false);
                    editor.apply();
                }
            }
        });

        Switch bt = findViewById(R.id.bt);
        bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(settings.this, "Working in Progress", Toast.LENGTH_SHORT).show();
                    bg.setChecked(false);
                } else{
                    Toast.makeText(settings.this, "Working in Progress", Toast.LENGTH_SHORT).show();
                    bg.setChecked(false);
                }
            }
        });
    }
}