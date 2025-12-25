package com.idol.prank.call.chat.video.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.idol.prank.call.chat.video.FirstMainApplication;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.utils.SharedPref;

public class SplashShow extends AppCompatActivity {

    private SharedPref sharedPref;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBarsplash);
        sharedPref = new SharedPref(this);

        // Get Application class
        FirstMainApplication app =
                (FirstMainApplication) getApplication();

        // 🔹 Preload App Open Ad
        app.loadAd(this);

        // 🔹 Splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // 🔹 Show App Open Ad after splash
            app.showAdIfAvailable(SplashShow.this, () -> {

                // 🔹 Navigate after ad is closed or fails
                if (sharedPref.getPolicyRead("yes").equals("yes")) {
                    startActivity(new Intent(SplashShow.this, AppWelcome.class));
                } else {
                    startActivity(new Intent(SplashShow.this, Privacy.class));
                }
                finish();

            });

        }, 3000);
    }
}
