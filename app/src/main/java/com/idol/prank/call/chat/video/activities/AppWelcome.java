package com.idol.prank.call.chat.video.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.idol.prank.call.chat.video.AdsModule.Constants;
import com.idol.prank.call.chat.video.R;

import java.util.concurrent.TimeUnit;

public class AppWelcome extends AppCompatActivity {
    private final String TAG = AppWelcome.class.getSimpleName();
    private Boolean checked = false;

    private Handler handlerRetryAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        showProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.cl).setVisibility(View.VISIBLE);
            }
        }, 3000);

        ImageView imageView = findViewById(R.id.anim);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_welcome)
                .into(imageView);

        ConstraintLayout next_button = findViewById(R.id.cl);

        handlerRetryAd = new Handler();

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppWelcome.this, Home.class);
                startActivity(i);
                finish();


            }
        });

    }


    @Override
    protected void onDestroy() {
        handlerRetryAd.removeCallbacksAndMessages(null);

        super.onDestroy();

    }

    public void startAct() {

        startActivity(new Intent(AppWelcome.this, Home.class));
        finish();


    }


    @SuppressLint("CutPasteId")
    @Override
    protected void onResume() {
        super.onResume();

        if (checked) {

        } else {

            if (Constants.isNetworkAvailable(this)) {

                ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle(getResources().getString(R.string.alert));
                progress.setMessage("Please wait...");
                progress.setCancelable(false);
            }

        }

    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void showProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Simulate some background task completion
        // For demonstration purposes, we'll dismiss the dialog after 3 seconds.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);
    }

}