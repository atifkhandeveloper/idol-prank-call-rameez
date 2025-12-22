package com.idol.prank.call.chat.video.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idol.prank.call.chat.video.R;


import com.idol.prank.call.chat.video.activities.fragments.LiveChat;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SettingOption extends AppCompatActivity {


  /*  private NativeAd nativeAd;
    private AdView adViewfbbanner;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;

    private  InterstitialAd fbInterstitialAd;

    private InterstitialAd fbInterstitialAdwed;*/

    private LinearLayout share,privacy,moreApps,rateUs,home;

    private Button back_button;

    int i=1;

    private int retry = 0;

/*    private com.facebook.ads.InterstitialAd interstitialAdfb = null;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        share = findViewById(R.id.share_button);
        home = findViewById(R.id.home_button);
        privacy = findViewById(R.id.privacy_button);
        moreApps = findViewById(R.id.more_apps);
        rateUs = findViewById(R.id.rate_us_button);
        back_button = findViewById(R.id.back_button);
        showProgressDialog();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(SettingOption.this , Home.class));



            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Constant.shareApp(SettingOption.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingOption.this, Privacy.class));
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(SettingOption.this, Home.class));
                }

        });

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Constant.rate(SettingScreen.this, "");
                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
                Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        moreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.moreApps(SettingOption.this,"Niklit Studio Apps");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  adsIntestial();
    }

/*    private void adsIntestial() {
        fbInterstitialAd =
                new InterstitialAd(this, getResources().getString(R.string.fbInterstitial));

        InterstitialAdListener interstitialAdListener=new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                BackPress();
            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        // load the ad
        fbInterstitialAd.loadAd(
                fbInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build()
        );
    }*/

 /*   private void FBInterstitial() {
        if (fbInterstitialAd == null || !fbInterstitialAd.isAdLoaded()) {
            BackPress();
        }
        if (fbInterstitialAd.isAdInvalidated()) {
            BackPress();
        } else {
            ProgressDialog progress =new  ProgressDialog(this);
            progress.setTitle(getResources().getString(R.string.alert));
            progress.setMessage("Please wait...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            Handler handler =new  Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    // Show the ad
                    fbInterstitialAd.show();
                }
            },2000);
        }
    }*/

    private void BackPress(){
        startActivity(new Intent(this,Home.class));
        finish();
    }

    @Override
    public void onBackPressed() {

            startActivity(new Intent(SettingOption.this , Home.class));

    }

    private void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);
    }


}