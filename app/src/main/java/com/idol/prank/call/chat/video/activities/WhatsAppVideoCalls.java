package com.idol.prank.call.chat.video.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.utils.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

public class WhatsAppVideoCalls extends AppCompatActivity implements SurfaceHolder.Callback {

    private TinyDBs tinyDB;
    private Handler handler;

    private LinearLayout atas, bawah;
    private TextView calling, nameuser;
    private CircleImageView circleImageView;
    private ImageView adduser;

    private RelativeLayout cancel, pesan, terima, tolak;
    private SurfaceView surfaceView, surfaceView2;
    private SurfaceHolder surfaceHolder;
    private VideoView videoView;

    private Camera camera;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen + lockscreen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        setContentView(R.layout.activity_whats_app_video_call_screen);

        tinyDB = new TinyDBs(this);
        handler = new Handler();

        // Views
        atas = findViewById(R.id.atas);
        bawah = findViewById(R.id.bawah);
        calling = findViewById(R.id.txtcall);
        nameuser = findViewById(R.id.txtname);
        circleImageView = findViewById(R.id.imguser);
        adduser = findViewById(R.id.adduser);
        adduser.setVisibility(View.INVISIBLE);

        // Video View
        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rainbow_video));
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));

        // Camera preview
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView2 = findViewById(R.id.surfaceView2);
        surfaceView2.setVisibility(View.GONE);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        // Receive character data
        receiveCharacterData();

        // Ringtone
        mp = MediaPlayer.create(this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        mp.setLooping(true);
        mp.start();

        // Buttons
        cancel = findViewById(R.id.layclose2);
        pesan = findViewById(R.id.laypesan);
        tolak = findViewById(R.id.layclose);
        terima = findViewById(R.id.layterima);

        cancel.setOnClickListener(v -> endCall());
        tolak.setOnClickListener(v -> endCall());

        pesan.setOnClickListener(v -> {
            stopSound();
            startActivity(new Intent(this, Home.class));
            finish();
        });

        terima.setOnClickListener(v -> acceptCall());
    }

    // ================= CHARACTER DATA =================

    private void receiveCharacterData() {
        String charName = "Caller";

        if (getIntent() != null && getIntent().hasExtra(Constant.EXTRA_CHAR_NAME)) {
            charName = getIntent().getStringExtra(Constant.EXTRA_CHAR_NAME);
            tinyDB.putString("char_name", charName);
        } else {
            charName = tinyDB.getString("char_name", "Caller");
        }

        nameuser.setText(charName);

        if (Constant.CHAR_BITMAP != null) {
            circleImageView.setImageBitmap(Constant.CHAR_BITMAP);
        } else {
            circleImageView.setImageResource(R.drawable.icon_splach_new);
        }
    }

    // ================= CALL ACTIONS =================

    private void acceptCall() {
        stopSound();

        calling.setVisibility(View.GONE);
        nameuser.setVisibility(View.GONE);
        circleImageView.setVisibility(View.GONE);
        adduser.setVisibility(View.VISIBLE);

        surfaceView.setVisibility(View.GONE);
        surfaceView2.setVisibility(View.VISIBLE);

        surfaceHolder = surfaceView2.getHolder();
        surfaceHolder.addCallback(this);

        videoView.start();
        atas.setVisibility(View.GONE);
        bawah.setVisibility(View.VISIBLE);
        tolak.setVisibility(View.VISIBLE);
    }

    private void endCall() {
        stopSound();
        startActivity(new Intent(this, Home.class));
        finish();
    }

    private void stopSound() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    // ================= CAMERA =================

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
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    // ================= BACK =================

    @Override
    public void onBackPressed() {
        stopSound();
        startActivity(new Intent(this, SelectVideoCall.class));
        finish();
    }
}
