package com.idol.prank.call.chat.video.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityHomeScreenBinding;
import com.idol.prank.call.chat.video.databinding.ActivitySelectCallingOptionsBinding;
import com.idol.prank.call.chat.video.databinding.ActivitySelectCallingOptionssBinding;
import com.idol.prank.call.chat.video.receiver.ReceiveCalls;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.Calendar;

public class SelectCall extends BaseActivity {

    // ===== CONSTANTS =====
    public static final int PLATFORM_WHATSAPP = 1;
    public static final int PLATFORM_FACEBOOK = 2;
    public static final int PLATFORM_SYSTEM = 3;

    public static final int PLATFORM_MESSENGER = 2; // <-- add this

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;

    public static int rd_form = 0;
    public static int rd_vid = 0;

    public static int rd_type = 0;  // selected call type
    public static int rd_time = 0;
    public static String status_time = "";

    private static final int ALARM_REQUEST_CODE = 134;

    // ===== UI =====
    private RadioGroup rgPlatform, rgTimer;
    private LinearLayout startCallBtn;
    private ActivitySelectCallingOptionsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCallingOptionsBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        setContentView(binding.getRoot());

        initViews();
        detectCallTypeFromHome();
        setupListeners();
    }

    private void initViews() {
        rgPlatform = findViewById(R.id.list_Template);
        rgTimer = findViewById(R.id.list_time);
        startCallBtn = findViewById(R.id.start_call_btn);
    }

    // ===== AUDIO / VIDEO FROM HOME =====
    private void detectCallTypeFromHome() {
        if (Constant.IS_VIDEO) {
            rd_vid = TYPE_VIDEO;
            Constant.IS_VIDEO = false;
        } else {
            rd_vid = TYPE_AUDIO;
            Constant.IS_VOICE = false;
        }
    }

    private void setupListeners() {

        // PLATFORM
        rgPlatform.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_whatsapp_button) {
                rd_form = PLATFORM_WHATSAPP;
            } else if (checkedId == R.id.radio_facebook_button) {
                rd_form = PLATFORM_FACEBOOK;
            } else if (checkedId == R.id.radio_whatsapp_button) {
                rd_form = PLATFORM_SYSTEM;
            }
        });

        // TIMER
        rgTimer.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_1) {
                rd_time = 1;
                status_time = "Calling now";
            } else if (checkedId == R.id.radio_10) {
                rd_time = 10;
                status_time = "Wait 10 seconds";
            } else if (checkedId == R.id.radio_30) {
                rd_time = 30;
                status_time = "Wait 30 seconds";
            } else if (checkedId == R.id.radio_60) {
                rd_time = 60;
                status_time = "Wait 1 minute";
            }
        });

        startCallBtn.setOnClickListener(v -> startCall());
    }

    // ===== MAIN LOGIC =====
    private void startCall() {

        if (rd_form == 0) {
            Toast.makeText(this, "Select Call Platform", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rd_time == 0) {
            Toast.makeText(this, "Select Timer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rd_time == 1) {
            openCallScreen();
        } else {
            scheduleCall();
        }
    }

    private void openCallScreen() {

        Intent intent = null;

        if (rd_form == PLATFORM_WHATSAPP) {
            intent = (rd_vid == TYPE_VIDEO)
                    ? new Intent(this, WhatsAppVideoCalls.class)
                    : new Intent(this, WhatsAppVideoCalls.class);
        }

        else if (rd_form == PLATFORM_FACEBOOK) {
            intent = (rd_vid == TYPE_VIDEO)
                    ? new Intent(this, FBVideoCallScreen.class)
                    : new Intent(this, FBVoiceCallScreen.class);
        }

        else if (rd_form == PLATFORM_SYSTEM) {
            intent = new Intent(this, CallSystem.class);
        }

        if (intent == null) {
            Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(intent);
        finish();
    }

    private void scheduleCall() {

        Intent alarmIntent = new Intent(this, ReceiveCalls.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                ALARM_REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, rd_time);

        ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, status_time, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Home.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
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
