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

import com.idol.prank.call.chat.video.AdsModule.TinyDBs;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityFaceBookVideoCallScreenBinding;
import com.idol.prank.call.chat.video.databinding.ActivityFaceBookVoiceCallScreenBinding;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class FBVoiceCallScreen extends BaseActivity {

    String string = "";
    private TinyDBs tinyDB;
    int MilliSeconds;
    private long MillisecondTime;
    private int Minutes;
    private int Seconds;
    private long StartTime;
    long TimeBuff;
    private LinearLayout atas;
    private LinearLayout bawah;
    private TextView calling;
    private ImageView imageView;
    private CircleImageView circleImageView;
    private Handler handler;
    private int hours;
    private ImageView imgback;
    private MediaPlayer mp;
    private RelativeLayout terima;
    private RelativeLayout tolak;
    private RelativeLayout tolak2;
    private long UpdateTime = 0;


    LinearLayout linearcalldecline;
    private ActivityFaceBookVoiceCallScreenBinding binding;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaceBookVoiceCallScreenBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(1140850688);
        setContentView(binding.getRoot());
        tinyDB = new TinyDBs(this);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        linearcalldecline = findViewById(R.id.declinefacebook);

        linearcalldecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FBVoiceCallScreen.this.startActivity(new Intent(FBVoiceCallScreen.this, Home.class));
                FBVoiceCallScreen.this.finish();
            }
        });

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

        this.handler = new Handler();
        this.atas = findViewById(R.id.caller_name);
        this.bawah = findViewById(R.id.laybawah2);
        this.calling = findViewById(R.id.txtwaktu);
        MediaPlayer create = MediaPlayer.create(this, RingtoneManager.getDefaultUri(R.raw.facebook_tune));
        this.mp = create;
        create.start();
        this.mp.setLooping(true);
        RelativeLayout relativeLayout = findViewById(R.id.laytolak);
        this.tolak = relativeLayout;
        relativeLayout.setOnClickListener(view -> {
            string = "end1";
            FBVoiceCallScreen.this.mp.stop();

        });
        ImageView imageView = findViewById(R.id.imgback2);
        this.imgback = imageView;
        imageView.setOnClickListener(view -> {
            FBVoiceCallScreen.this.startActivity(new Intent(FBVoiceCallScreen.this, Home.class));
            FBVoiceCallScreen.this.finish();
            FBVoiceCallScreen.this.mp.stop();
        });
        RelativeLayout relativeLayout2 = findViewById(R.id.laytolak2);
        this.tolak2 = relativeLayout2;
        relativeLayout2.setOnClickListener(view -> {
            string = "end2";
            FBVoiceCallScreen.this.mp.stop();

        });
        RelativeLayout relativeLayout3 = findViewById(R.id.layterima);
        this.terima = relativeLayout3;
        relativeLayout3.setOnClickListener(view -> {
            FBVoiceCallScreen.this.StartTime = SystemClock.uptimeMillis();
            FBVoiceCallScreen.this.handler.postDelayed(FBVoiceCallScreen.this.runnable, 0L);
            FBVoiceCallScreen.this.atas.setVisibility(View.GONE);
            FBVoiceCallScreen.this.bawah.setVisibility(View.VISIBLE);
            FBVoiceCallScreen.this.mp.stop();
            FBVoiceCallScreen.this.mp = new MediaPlayer();
            FBVoiceCallScreen.this.mp = MediaPlayer.create(FBVoiceCallScreen.this, R.raw.burno_voice);
            FBVoiceCallScreen.this.mp.setLooping(true);
            FBVoiceCallScreen.this.mp.start();
        });

        this.circleImageView = findViewById(R.id.fbimguser);
        this.imageView = findViewById(R.id.imgback);



        if (Constant.CHAR_BITMAP != null) {
            this.circleImageView.setImageBitmap(Constant.CHAR_BITMAP);
            this.imageView.setImageBitmap(Constant.CHAR_BITMAP);
        } else {
            this.circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_splach_new));
            //this.imageView.setImageDrawable(getResources().getDrawable(R.drawable.char1));
        }
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            FBVoiceCallScreen.this.MillisecondTime = SystemClock.uptimeMillis() - FBVoiceCallScreen.this.StartTime;
            FBVoiceCallScreen fBVoiceCallActivity = FBVoiceCallScreen.this;
            fBVoiceCallActivity.UpdateTime = fBVoiceCallActivity.TimeBuff + FBVoiceCallScreen.this.MillisecondTime;
            FBVoiceCallScreen fBVoiceCallActivity2 = FBVoiceCallScreen.this;
            fBVoiceCallActivity2.Seconds = (int) (fBVoiceCallActivity2.UpdateTime / 1000);
            FBVoiceCallScreen fBVoiceCallActivity3 = FBVoiceCallScreen.this;
            fBVoiceCallActivity3.Minutes = fBVoiceCallActivity3.Seconds / 60;
            FBVoiceCallScreen.this.Seconds %= 60;
            FBVoiceCallScreen fBVoiceCallActivity4 = FBVoiceCallScreen.this;
            fBVoiceCallActivity4.hours = fBVoiceCallActivity4.Minutes / 60;
            FBVoiceCallScreen fBVoiceCallActivity5 = FBVoiceCallScreen.this;
            fBVoiceCallActivity5.MilliSeconds = (int) (fBVoiceCallActivity5.UpdateTime % 1000);
            FBVoiceCallScreen.this.calling.setText(String.format("%02d", Integer.valueOf(FBVoiceCallScreen.this.hours)) + ":" + String.format("%02d", Integer.valueOf(FBVoiceCallScreen.this.Minutes)) + ":" + String.format("%02d", Integer.valueOf(FBVoiceCallScreen.this.Seconds)));
            FBVoiceCallScreen.this.handler.postDelayed(this, 0L);
        }
    };

    @Override
    public void onBackPressed() {
        this.mp.stop();
        startActivity(new Intent(this, SelectCall.class));
        finish();
    }

}