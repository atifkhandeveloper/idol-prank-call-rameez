package com.idol.prank.call.chat.video.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.activities.fragments.LiveChat;
import com.idol.prank.call.chat.video.databinding.ActivityHomeScreenBinding;
import com.idol.prank.call.chat.video.databinding.ActivityWelcomeScreenBinding;
import com.idol.prank.call.chat.video.utils.Constant;

public class Home extends BaseActivity {

    RelativeLayout voice_call_button, video_call_button, chat;
    ImageView settings;
    FrameLayout templateview;
    private ActivityHomeScreenBinding binding;


    private InterstitialAd interstitialAd;
    private boolean isAdShowing = false;
    private long lastAdTime = 0;
    private final int AD_COOLDOWN_MS = 15000; // 15 seconds cooldown

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        setContentView(binding.getRoot());

        takePermission();

        if (PremiumManager.INSTANCE.shouldShowAds(this)) {
//            showProgressDialog();
            loadNativeAd();
            loadInterstitial(); // preload first ad

        }
        showGoogleRateDialog(this);

        chat = findViewById(R.id.btn_message);
        voice_call_button = findViewById(R.id.btn_audio_call);
        video_call_button = findViewById(R.id.btn_video_call);
        settings = findViewById(R.id.iv_settings);
        templateview = findViewById(R.id.native_ad_frame_Main);
        templateview.setVisibility(GONE);

        MobileAds.initialize(this);

        if (PremiumManager.INSTANCE.isPremium(this)) {
            binding.pro.setVisibility(GONE);
        }else{
            binding.pro.setVisibility(VISIBLE);
        }


        voice_call_button.setOnClickListener(v ->
                showInterstitialWithCooldown(() -> startActivity(new Intent(Home.this, CharSelection.class))));

        video_call_button.setOnClickListener(v ->
                showInterstitialWithCooldown(() -> startActivity(new Intent(Home.this, CharSelection.class))));

        chat.setOnClickListener(v ->
                showInterstitialWithCooldown(() -> startActivity(new Intent(Home.this, LiveChat.class))));

        settings.setOnClickListener(v ->
                startActivity(new Intent(Home.this, SettingOption.class)));

        binding.pro.setOnClickListener(v ->
                startActivity(new Intent(Home.this, PremiumActivity.class)));
    }

    // ------------------- INTERSTITIAL -------------------

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(
                this,
                getString(R.string.interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd ad) {
                        interstitialAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        interstitialAd = null;
                    }
                });
    }

    private void showInterstitialWithCooldown(Runnable onComplete) {
        long currentTime = System.currentTimeMillis();

        // If cooldown not passed or ad not ready, open directly
        if (isAdShowing || (currentTime - lastAdTime < AD_COOLDOWN_MS) || interstitialAd == null) {
            onComplete.run();
        } else {
            isAdShowing = true;

            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Ad closed, go to next activity
                    isAdShowing = false;
                    lastAdTime = System.currentTimeMillis();
                    interstitialAd = null;

                    // Preload next interstitial
                    loadInterstitial();

                    // Now move to next activity
                    onComplete.run();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    isAdShowing = false;
                    interstitialAd = null;
                    loadInterstitial();
                    onComplete.run();
                }
            });

            interstitialAd.show(this);
        }
    }

    // ------------------- NATIVE AD -------------------

    private void loadNativeAd() {
        com.google.android.gms.ads.AdLoader adLoader = new com.google.android.gms.ads.AdLoader.Builder(this, getResources().getString(R.string.nativead))
                .forNativeAd(nativeAd -> {
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    TemplateView template = findViewById(R.id.my_template);
                    templateview.setVisibility(VISIBLE);
                    template.setStyles(styles);
                    template.setNativeAd(nativeAd);
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    // ------------------- PERMISSIONS -------------------

    private void takePermission() {
        if (Build.VERSION.SDK_INT >= 28 && !Settings.canDrawOverlays(this)) {
            checkOverlayPermission();
        }
        if (Build.VERSION.SDK_INT < 26) {
            new WindowManager.LayoutParams(-2, -2, 2002, 40, -2);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.CAMERA"}, 1);
        }
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) return;
        startActivityForResult(
                new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())),
                5469);
    }

    // ------------------- BACK PRESS -------------------

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finishAffinity())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    public void showGoogleRateDialog(Activity activity) {

        ReviewManager manager = ReviewManagerFactory.create(activity);

        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);

                flow.addOnCompleteListener(task2 -> {
                    // Review flow finished (no need to check result)
                });

            } else {
                // Handle error if needed
            }
        });
    }
}
