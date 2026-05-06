package com.idol.prank.call.chat.video.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.idol.prank.call.chat.video.AdsModule.Constants;
import com.idol.prank.call.chat.video.AdsModule.TinyDBs;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityMainBinding;
import com.idol.prank.call.chat.video.databinding.ActivityWhatsAppVoiceCallScreenBinding;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class WhatsAppCalls extends BaseActivity {



    String string = "";
    private TinyDBs tinyDB;
    int MilliSeconds;
    long MillisecondTime;
    int Minutes;
    int Seconds;
    long StartTime;
    long TimeBuff;
    private ImageView adduser;
    private LinearLayout atas;
    private LinearLayout bawah;
    private TextView calling;
    private RelativeLayout cancel;
    CircleImageView circleImageView;
    Handler handler;
    int hours;
    private ImageView imguser2;
    MediaPlayer mp;
    private RelativeLayout pesan;
    private RelativeLayout terima;
    private RelativeLayout rlCancelCall;
    long UpdateTime = 0;
    private int retry = 0;
    private LinearLayout mLyAds;
    private ActivityWhatsAppVoiceCallScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsAppVoiceCallScreenBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(1140850688);
        setContentView(binding.getRoot());

        tinyDB = new TinyDBs(this);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {

       /*     getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);*/
        }


        mLyAds = findViewById(R.id.llShowAdsProgress);



        this.atas = findViewById(R.id.atas);
        this.bawah = findViewById(R.id.bawah);
        this.calling = findViewById(R.id.txtcall);
        this.imguser2 = findViewById(R.id.imguser2);
        ImageView imageView = findViewById(R.id.adduser);
        this.adduser = imageView;
        imageView.setVisibility(View.INVISIBLE);
        this.cancel = findViewById(R.id.layclose2);
        this.rlCancelCall = findViewById(R.id.layclose);
        RelativeLayout relativeLayout = findViewById(R.id.laypesan);
        this.pesan = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatsAppCalls.this.mp.stop();
              //  WhatsAppCalls.this.startActivity(new Intent(WhatsAppCalls.this, Home.class));
               // WhatsAppCalls.this.finish();
            }
        });
        this.handler = new Handler();
        this.rlCancelCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "end1";
                WhatsAppCalls.this.mp.stop();

                    startActivity(new Intent(WhatsAppCalls.this , Home.class));
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "end2";
                WhatsAppCalls.this.mp.stop();

                    startActivity(new Intent(WhatsAppCalls.this , Home.class));

            }
        });
        MediaPlayer create = MediaPlayer.create(getApplicationContext(), RingtoneManager.getDefaultUri(1));
        this.mp = create;
        create.start();
        this.mp.setLooping(true);
        this.circleImageView = findViewById(R.id.ivUser);

        if (Constant.CHAR_BITMAP != null) {
            this.circleImageView.setImageBitmap(Constant.CHAR_BITMAP);
            imguser2.setImageBitmap(Constant.CHAR_BITMAP);
        } else {
            this.circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_splach_new));
            imguser2.setImageDrawable(getResources().getDrawable(R.drawable.what_icon_img));
        }

        RelativeLayout relativeLayout2 = findViewById(R.id.layterima);
        this.terima = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatsAppCalls.this.atas.setVisibility(View.GONE);
                WhatsAppCalls.this.rlCancelCall.setVisibility(View.VISIBLE);
                WhatsAppCalls.this.bawah.setVisibility(View.VISIBLE);
                WhatsAppCalls.this.imguser2.setVisibility(View.VISIBLE);
                WhatsAppCalls.this.StartTime = SystemClock.uptimeMillis();
                WhatsAppCalls.this.handler.postDelayed(WhatsAppCalls.this.runnable, 0L);
                String str = null;
                WhatsAppCalls.this.mp.stop();
                WhatsAppCalls.this.mp = new MediaPlayer();
                WhatsAppCalls.this.mp = MediaPlayer.create(WhatsAppCalls.this, R.raw.burno_voice);
                WhatsAppCalls.this.mp.setLooping(true);
                WhatsAppCalls.this.mp.start();

            }
        });
    }

    public Runnable runnable = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {
            WhatsAppCalls.this.MillisecondTime = SystemClock.uptimeMillis() - WhatsAppCalls.this.StartTime;
            WhatsAppCalls wAVoiceCallActivity = WhatsAppCalls.this;
            wAVoiceCallActivity.UpdateTime = wAVoiceCallActivity.TimeBuff + WhatsAppCalls.this.MillisecondTime;
            WhatsAppCalls wAVoiceCallActivity2 = WhatsAppCalls.this;
            wAVoiceCallActivity2.Seconds = (int) (wAVoiceCallActivity2.UpdateTime / 1000);
            WhatsAppCalls wAVoiceCallActivity3 = WhatsAppCalls.this;
            wAVoiceCallActivity3.Minutes = wAVoiceCallActivity3.Seconds / 60;
            WhatsAppCalls.this.Seconds %= 60;
            WhatsAppCalls wAVoiceCallActivity4 = WhatsAppCalls.this;
            wAVoiceCallActivity4.hours = wAVoiceCallActivity4.Minutes / 60;
            WhatsAppCalls wAVoiceCallActivity5 = WhatsAppCalls.this;
            wAVoiceCallActivity5.MilliSeconds = (int) (wAVoiceCallActivity5.UpdateTime % 1000);
            WhatsAppCalls.this.calling.setText(String.format("%02d", Integer.valueOf(WhatsAppCalls.this.hours)) + ":" + String.format("%02d", Integer.valueOf(WhatsAppCalls.this.Minutes)) + ":" + String.format("%02d", Integer.valueOf(WhatsAppCalls.this.Seconds)));
            WhatsAppCalls.this.handler.postDelayed(this, 0L);
        }
    };

    private void closeActivity() {
        WhatsAppCalls.this.startActivity(new Intent(WhatsAppCalls.this, Home.class));
        WhatsAppCalls.this.finish();
    }

    private void startActivity(){
        if(string.contains("end1")){
            closeActivity();
        }
        else if(string.contains("end2")){
            WhatsAppCalls.this.startActivity(new Intent(WhatsAppCalls.this, Home.class));
            WhatsAppCalls.this.finish();
        }
    }

    private void FBInterstitial() {

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

                }
            },2000);

    }

    private void BackPress(){
        startActivity(new Intent(this, SelectCall.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        this.mp.stop();
        FBInterstitial();
    }
}