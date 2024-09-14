package com.example.appstore.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },2000);
    }

    private void nextActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppStore", MODE_PRIVATE);
        String user = sharedPreferences.getString("id", "");
        if (user != null && !user.trim().equals("")) {
            Log.i("TAG1", "nextActivity: " + user);
            startActivity(new Intent(Splash.this, MainActivity.class));
            finishAffinity();
        } else {
            startActivity(new Intent(Splash.this,Login.class));
            finishAffinity();
        }
    }
}