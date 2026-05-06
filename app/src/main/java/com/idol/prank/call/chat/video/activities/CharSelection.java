package com.idol.prank.call.chat.video.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityCharacterSelectBinding;
import com.idol.prank.call.chat.video.databinding.ActivityWelcomeScreenBinding;
import com.idol.prank.call.chat.video.utils.Constant;

public class CharSelection extends BaseActivity {

    TemplateView template;
    private ActivityCharacterSelectBinding binding;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCharacterSelectBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        setContentView(binding.getRoot());

        template = findViewById(R.id.my_template);
        template.setVisibility(GONE);
        loadNativeAd();

        // Character Clicks
        setupCharacterClick(R.id.char1, 1, "Santa", R.drawable.one);
        setupCharacterClick(R.id.char2, 2, "Hikari", R.drawable.two);
        setupCharacterClick(R.id.char3, 3, "Nanami", R.drawable.three);
        setupCharacterClick(R.id.char4, 4, "Daichi", R.drawable.four);
        setupCharacterClick(R.id.char5, 5, "Kaori", R.drawable.five);
        setupCharacterClick(R.id.char6, 6, "Takumi", R.drawable.six);

        // Back
        findViewById(R.id.btc).setOnClickListener(v -> {
            startActivity(new Intent(CharSelection.this, Home.class));
            finish();
        });
    }

    private void setupCharacterClick(int viewId, int charNo, String charName, int drawableRes) {
        findViewById(viewId).setOnClickListener(v -> {

            // Save globally
            Constant.character_no = charNo;
            Constant.CHAR_BITMAP =
                    BitmapFactory.decodeResource(getResources(), drawableRes);

            Log.d("CharSelection", "Selected: " + charName);

            // Send to next activity
            Intent intent = new Intent(CharSelection.this, CombineMakeCallActivity.class);
            intent.putExtra(Constant.EXTRA_CHAR_NAME, charName);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constant.isNetworkAvailable(this)) {
            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Alert");
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                findViewById(R.id.native_ad_frame_Char1).setVisibility(View.VISIBLE);
                findViewById(R.id.native_ad_frame_Char2).setVisibility(View.VISIBLE);
                if (!isFinishing()) progress.dismiss();
            }, 4000);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
        finish();
    }

    private void loadNativeAd() {
        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.nativead))
                .forNativeAd(nativeAd -> {
                    NativeTemplateStyle styles =
                            new NativeTemplateStyle.Builder().build();
                    template.setVisibility(VISIBLE);
                    template.setStyles(styles);
                    template.setNativeAd(nativeAd);
                }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
