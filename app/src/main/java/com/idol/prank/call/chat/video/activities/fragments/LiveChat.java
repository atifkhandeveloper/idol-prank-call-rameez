package com.idol.prank.call.chat.video.activities.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.activities.Home;
import com.idol.prank.call.chat.video.activities.PremiumManager;
import com.idol.prank.call.chat.video.utils.Constant;

import android.os.Bundle;

public class LiveChat extends AppCompatActivity {

    private EditText messageEditText;
    private LinearLayout chatHistoryLinearLayout;
    private Button backButton;
    private AdView adView;
    private LinearLayout adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_layout);

        backButton = findViewById(R.id.back_button);
        messageEditText = findViewById(R.id.question_text_input_edit_text);
        chatHistoryLinearLayout = findViewById(R.id.chat_history_linear_layout);
        Button sendButton = findViewById(R.id.submit_question_button);
        adContainer = findViewById(R.id.ad_container);

        MobileAds.initialize(this);

        backButton.setOnClickListener(v ->
                startActivity(new Intent(LiveChat.this, Home.class)));

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();
            if (!TextUtils.isEmpty(message)) {
                String response = AutoBot.getResponse(message);
                addMessageToChat(message, response);
                messageEditText.setText("");
            }
        });



        if (PremiumManager.INSTANCE.shouldShowAds(this)) {
            if (Constant.isNetworkAvailable(this)) {
                loadAdaptiveCollapsibleBanner();
            }
        }
    }

    // ------------------ CHAT UI ------------------

    private void addMessageToChat(String message, String response) {

        TextView userTextView = new TextView(this);
        userTextView.setText(message);
        userTextView.setTextColor(Color.WHITE);
        userTextView.setBackgroundResource(R.drawable.bot_message_bg);
        userTextView.setPadding(15, 15, 15, 15);
        userTextView.setGravity(Gravity.START);

        TextView botTextView = new TextView(this);
        botTextView.setText(response);
        botTextView.setTextColor(Color.WHITE);
        botTextView.setBackgroundResource(R.drawable.user_message_bg);
        botTextView.setPadding(15, 15, 15, 15);
        botTextView.setGravity(Gravity.END);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(userTextView);
        layout.addView(botTextView);

        chatHistoryLinearLayout.addView(layout);
    }

    // ------------------ COLLAPSIBLE BANNER ------------------

    private void loadAdaptiveCollapsibleBanner() {

        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_id));

        adContainer.removeAllViews();
        adContainer.addView(adView);

        adContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int adWidthPixels = adContainer.getWidth();
                        if (adWidthPixels == 0) return;

                        float density = getResources().getDisplayMetrics().density;
                        int adWidth = (int) (adWidthPixels / density);

                        AdSize adSize =
                                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                                        LiveChat.this, adWidth);

                        adView.setAdSize(adSize);
                        loadCollapsibleBanner();

                        adContainer.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void loadCollapsibleBanner() {

        Bundle extras = new Bundle();
        extras.putString("collapsible", "bottom");

        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Collapsible banner loaded
            }
        });

        adView.loadAd(adRequest);

        adView.setOnPaidEventListener(new OnPaidEventListener() {
            @Override
            public void onPaidEvent(AdValue adValue) {

                double revenue = adValue.getValueMicros() / 1000000.0;
                String currency = adValue.getCurrencyCode();

                Log.d("Ads", "Native Revenue: " + revenue + " " + currency);

                sendRevenueToFirebase(revenue, currency);
            }
        });
    }

    // ------------------ BACK ------------------

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LiveChat.this, Home.class));
        finish();
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
