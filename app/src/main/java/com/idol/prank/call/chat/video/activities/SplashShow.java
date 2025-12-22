package com.idol.prank.call.chat.video.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.idol.prank.call.chat.video.R;


import com.idol.prank.call.chat.video.utils.SharedPref;

import java.util.concurrent.TimeUnit;

public class SplashShow extends AppCompatActivity  {
    private SharedPref sharedPref;

ProgressBar progressBar;

    private ProgressDialog progressDialog;

    private int retry = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBarsplash);
        sharedPref = new SharedPref(this);
        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPref.getPolicyRead("yes").equals("yes")){
                    startActivity(new Intent(getApplicationContext(), AppWelcome.class));
                    finish();
                } else{

                    startActivity(new Intent(getApplicationContext(), Privacy.class));


                }

            }
        },1000);

    }

}