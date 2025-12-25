package com.idol.prank.call.chat.video.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.receiver.ReceiveCalls;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.Calendar;

public class CombineMakeCallActivity extends AppCompatActivity {

    // UI
    RadioGroup radioGroupCalls, radioGroupTime;
    CardView callNowCard;
    ImageView backIcon, characterImage;
    TextView characterNameText;
    TemplateView template;

    RadioButton audioBtn, videoBtn;

    // Logic
    private String selectedCharName = "";
    private ProgressDialog progressDialog;
    private static final int ALARM_REQUEST_CODE = 134;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_make_call);

        // ===== INIT UI =====
        radioGroupCalls = findViewById(R.id.list_calls);
        radioGroupTime = findViewById(R.id.list_time);
        callNowCard = findViewById(R.id.startcallcard);

        audioBtn = findViewById(R.id.radio_whatsapp_buttoncal);
        videoBtn = findViewById(R.id.radio_facebook_buttoncal);

        backIcon = findViewById(R.id.backicon);
        characterNameText = findViewById(R.id.texttemplate);
        characterImage = findViewById(R.id.char1);

        template = findViewById(R.id.my_template);
        template.setVisibility(GONE);

        // ===== RECEIVE CHARACTER FIRST =====
        receiveCharacterData();

        // ===== ADS & LOADER =====
        showProgressDialog();
        loadNativeAd();

        // ===== BACK =====
        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, CharSelection.class));
            finish();
        });

        // ===== CALL TYPE =====
        radioGroupCalls.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_whatsapp_buttoncal) {
                SelectCall.rd_form = 1; // WhatsApp
            } else if (checkedId == R.id.radio_facebook_buttoncal) {
                SelectCall.rd_form = 2; // Facebook
            }
        });

        // ===== TIMER =====
        radioGroupTime.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_1) {
                SelectCall.rd_time = 1;
                SelectCall.status_time = "Calling now";
            } else if (checkedId == R.id.radio_10) {
                SelectCall.rd_time = Constant.TIMER_A;
                SelectCall.status_time = "Wait 10 seconds";
            } else if (checkedId == R.id.radio_30) {
                SelectCall.rd_time = Constant.TIMER_B;
                SelectCall.status_time = "Wait 30 seconds";
            } else if (checkedId == R.id.radio_60) {
                SelectCall.rd_time = Constant.TIMER_C;
                SelectCall.status_time = "Wait 1 minute";
            }
        });

        // ===== START CALL =====
        callNowCard.setOnClickListener(v -> startCall());
    }

    // ================= CHARACTER =================

    private void receiveCharacterData() {
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Constant.EXTRA_CHAR_NAME)) {
            selectedCharName = intent.getStringExtra(Constant.EXTRA_CHAR_NAME);
        }

        if (selectedCharName == null || selectedCharName.isEmpty()) {
            selectedCharName = "Unknown Caller";
        }

        characterNameText.setText(selectedCharName);

        Bitmap bitmap = Constant.CHAR_BITMAP;
        if (bitmap != null) {
            characterImage.setImageBitmap(bitmap);
        }
    }

    // ================= CALL LOGIC =================

    private void startCall() {

        if (radioGroupCalls.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select Call Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioGroupTime.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select Timer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (SelectCall.rd_time == 1) {
            openCallScreen();
        } else {
            scheduleCall();
        }
    }

    private void scheduleCall() {

        Intent alarmIntent = new Intent(this, ReceiveCalls.class);
        alarmIntent.putExtra(Constant.EXTRA_CHAR_NAME, selectedCharName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                ALARM_REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, SelectCall.rd_time);

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        Toast.makeText(this, SelectCall.status_time, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Home.class));
        finish();
    }

    private void openCallScreen() {

        Intent intent;

        if (SelectCall.rd_form == 1) {
            intent = new Intent(this, WhatsAppVideoCalls.class);
        } else {
            intent = new Intent(this, FBVideoCallScreen.class);
        }

        intent.putExtra(Constant.EXTRA_CHAR_NAME, selectedCharName);
        startActivity(intent);
        finish();
    }

    // ================= UTILS =================

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(() -> {
            if (!isFinishing()) progressDialog.dismiss();
        }, 3000);
    }

    private void loadNativeAd() {
        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.nativead))
                .forNativeAd(nativeAd -> {
                    NativeTemplateStyle style =
                            new NativeTemplateStyle.Builder().build();
                    template.setVisibility(VISIBLE);
                    template.setStyles(style);
                    template.setNativeAd(nativeAd);
                }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, CharSelection.class));
        finish();
    }
}
