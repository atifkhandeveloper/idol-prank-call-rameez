package com.idol.prank.call.chat.video.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.idol.prank.call.chat.video.AdsModule.Constants;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityWelcomeScreenBinding;

public class AppWelcome extends BaseActivity {

    private InterstitialAd interstitialAd;
    private RelativeLayout templateview;
    private ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        setContentView(binding.getRoot());

        showProgressDialog();

        templateview = findViewById(R.id.relativeLayoutadmob);
        templateview.setVisibility(GONE);

        loadNativeAd();
        loadInterstitialAd();

        ImageView imageView = findViewById(R.id.anim);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_welcome)
                .into(imageView);

        ConstraintLayout nextButton = findViewById(R.id.cl);

        new Handler().postDelayed(() -> nextButton.setVisibility(VISIBLE), 3000);

        nextButton.setOnClickListener(v -> showInterstitialOrGoNext());
    }

    // ---------------- INTERSTITIAL ----------------

    private void loadInterstitialAd() {
        MobileAds.initialize(this);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(
                this,
                getString(R.string.interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {

                    @Override
                    public void onAdLoaded(InterstitialAd ad) {
                        interstitialAd = ad;
                        setInterstitialCallback();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        interstitialAd = null;
                    }
                }
        );
    }

    private void setInterstitialCallback() {
        if (interstitialAd == null) return;

        interstitialAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        interstitialAd = null;
                        openHome();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        interstitialAd = null;
                        openHome();
                    }
                }
        );
    }

    private void showInterstitialOrGoNext() {
        if (Constants.isNetworkAvailable(this) && interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            openHome();
        }
    }

    // ---------------- NAVIGATION ----------------

    private void openHome() {
        startActivity(new Intent(AppWelcome.this, Home.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // disable back
    }

    // ---------------- NATIVE AD ----------------

    private void loadNativeAd() {
        MobileAds.initialize(this);

        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.nativead))
                .forNativeAd(nativeAd -> {
                    NativeTemplateStyle styles =
                            new NativeTemplateStyle.Builder().build();
                    TemplateView template = findViewById(R.id.my_template);
                    templateview.setVisibility(VISIBLE);
                    template.setStyles(styles);
                    template.setNativeAd(nativeAd);
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    // ---------------- PROGRESS ----------------

    private void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(progressDialog::dismiss, 3000);
    }
}
