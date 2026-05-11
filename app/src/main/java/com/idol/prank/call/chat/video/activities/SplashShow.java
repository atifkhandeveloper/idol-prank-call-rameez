package com.idol.prank.call.chat.video.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.FirstMainApplication;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityMainBinding;
import com.idol.prank.call.chat.video.databinding.ActivitySettingScreenBinding;
import com.idol.prank.call.chat.video.utils.SharedPref;

public class SplashShow extends BaseActivity {

    private SharedPref sharedPref;
    private ProgressBar progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBarsplash);
        sharedPref = new SharedPref(this);

        // Get Application class
        FirstMainApplication app =
                (FirstMainApplication) getApplication();

        // 🔹 Preload App Open Ad

        if (PremiumManager.INSTANCE.shouldShowAds(this)) {

            app.loadAd(this);

        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

    public void sendRevenueToFirebase(double value, String currency) {

        Bundle bundle = new Bundle();
        bundle.putDouble("value", value);
        bundle.putString("currency", currency);
        bundle.putString("ad_platform", "admob");
        bundle.putString("ad_source", "admob");
        bundle.putString("ad_format", "native"); // IMPORTANT

        FirebaseAnalytics.getInstance(this)
                .logEvent("ad_impression", bundle);
    }
}
