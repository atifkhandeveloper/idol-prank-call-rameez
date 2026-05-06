package com.idol.prank.call.chat.video.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.idol.prank.call.chat.video.AdsModule.TinyDBs;
import com.idol.prank.call.chat.video.BaseActivity;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.databinding.ActivityCombineMakeCallBinding;
import com.idol.prank.call.chat.video.databinding.ActivityFaceBookVideoCallScreenBinding;
import com.idol.prank.call.chat.video.utils.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

public class FBVideoCallScreen extends BaseActivity implements SurfaceHolder.Callback {

    private TinyDBs tinyDB;
    private LinearLayout atas, bawah;
    private TextView calling;
    private CircleImageView gambrH;
    private ImageView gambrB, imgback;
    private MediaPlayer mp;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private VideoView videoView;
    private Camera camera;
    private RelativeLayout terima, tolak, tolak2;
    private Handler handler;

    private String charName = "Caller";
    private ActivityFaceBookVideoCallScreenBinding binding;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaceBookVideoCallScreenBinding.inflate(getLayoutInflater());
        enableEdgeToEdge();
        applyEdgeToEdgePadding(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(1140850688);
        setContentView(binding.getRoot());

        tinyDB = new TinyDBs(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        // Views
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setVisibility(View.GONE);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        videoView = findViewById(R.id.videoView);
        videoView.setMediaController(null);
        videoView.requestFocus();

        atas = findViewById(R.id.layutama);
        bawah = findViewById(R.id.laybawah2);
        calling = findViewById(R.id.caller_name);
        gambrH = findViewById(R.id.fbimguser);
        gambrB = findViewById(R.id.imgback);
        imgback = findViewById(R.id.imgback2);

        terima = findViewById(R.id.layterima);
        tolak = findViewById(R.id.laytolak);
        tolak2 = findViewById(R.id.laytolak2);

        handler = new Handler();

        // Receive Character Data
        receiveCharacterData();

        // Ringtone
        mp = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        mp.setLooping(true);
        mp.start();

        // Button listeners
        terima.setOnClickListener(v -> acceptCall());
        tolak.setOnClickListener(v -> endCall());
        tolak2.setOnClickListener(v -> endCall());
        imgback.setOnClickListener(v -> {
            stopSound();
            startActivity(new Intent(FBVideoCallScreen.this, Home.class));
            finish();
        });
    }

    private void receiveCharacterData() {
        if (getIntent() != null && getIntent().hasExtra(Constant.EXTRA_CHAR_NAME)) {
            charName = getIntent().getStringExtra(Constant.EXTRA_CHAR_NAME);
        } else {
            charName = tinyDB.getString("char_name", "Caller");
        }

        // Set character name
        calling.setText(charName);

        // Set character profile image
        switch (charName) {
            case "Santa":
                gambrH.setImageResource(R.drawable.one);
                gambrB.setImageResource(R.drawable.skip_button);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.santa));
                break;
            case "Hikari":
                gambrH.setImageResource(R.drawable.two);
                gambrB.setImageResource(R.drawable.skip_button);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hikari));
                break;
            case "Nanami":
                gambrH.setImageResource(R.drawable.three);
                gambrB.setImageResource(R.drawable.skip_button);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nanami));
                break;
            case "Daichi":
                gambrH.setImageResource(R.drawable.four);
                gambrB.setImageResource(R.drawable.skip_button);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.daichi));
                break;
            case "Kaori":
                gambrH.setImageResource(R.drawable.five);
                gambrB.setImageResource(R.drawable.skip_button);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kaori));
                break;
            case "Takumi":
                gambrH.setImageResource(R.drawable.six);
                gambrB.setImageResource(R.drawable.skip_button);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.takumi));
                break;
            default:
                gambrH.setImageResource(R.drawable.icon_splach_new);
                gambrB.setImageResource(R.drawable.icon_splach_new);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rainbow_video));
                break;
        }
    }

    private void acceptCall() {
        stopSound();
        surfaceView.setVisibility(View.VISIBLE);
        atas.setVisibility(View.GONE);
        bawah.setVisibility(View.VISIBLE);
        gambrB.setVisibility(View.GONE);

        // Start character video
        videoView.start();
    }

    private void endCall() {
        stopSound();
        startActivity(new Intent(FBVideoCallScreen.this, Home.class));
        finish();
    }

    private void stopSound() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            camera = Camera.open(1);
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {
        stopSound();
        startActivity(new Intent(this, SelectVideoCall.class));
        finish();
    }
}
