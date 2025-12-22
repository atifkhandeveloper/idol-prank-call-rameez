package com.idol.prank.call.chat.video.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.idol.prank.call.chat.video.AdsModule.Constants;
import com.idol.prank.call.chat.video.AdsModule.TinyDBs;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class WhatsAppVideoCalls extends AppCompatActivity implements SurfaceHolder.Callback {


    String string = "";
    private TinyDBs tinyDB;
    private ImageView adduser;
    private LinearLayout atas;
    private LinearLayout bawah;
    private TextView calling;
    Camera camera;
    private RelativeLayout cancel;
    Handler handler;
    private CircleImageView circleImageView;
    MediaPlayer mp;
    private TextView nameuser;
    private RelativeLayout pesan;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    SurfaceView surfaceView2;
    private RelativeLayout terima;
    private RelativeLayout tolak;
    VideoView videoView;

    private int retry = 0;
    //ads

    private LinearLayout mLyAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(1140850688);
        setContentView(R.layout.activity_whats_app_video_call_screen);

        tinyDB = new TinyDBs(this);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
        }

        mLyAds = findViewById(R.id.llShowAdsProgress);


        MediaPlayer create = MediaPlayer.create(getApplicationContext(), RingtoneManager.getDefaultUri(1));
        this.mp = create;
        create.start();
        this.mp.setLooping(true);
        this.atas = findViewById(R.id.atas);
        this.bawah = findViewById(R.id.bawah);
        this.videoView = findViewById(R.id.videoView);
        this.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rainbow_video));
        this.videoView.requestFocus();

        this.surfaceView = findViewById(R.id.surfaceView);
        SurfaceView surfaceView = findViewById(R.id.surfaceView2);
        this.surfaceView2 = surfaceView;
        surfaceView.setVisibility(View.GONE);
        SurfaceHolder holder = this.surfaceView.getHolder();
        this.surfaceHolder = holder;
        holder.addCallback(this);
        this.surfaceHolder.setFormat(-1);
        this.surfaceHolder.setType(View.VISIBLE);
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                WhatsAppVideoCalls.this.surfaceView.setVisibility(View.VISIBLE);
                float videoWidth = mediaPlayer.getVideoWidth() / mediaPlayer.getVideoHeight();
                float width = WhatsAppVideoCalls.this.videoView.getWidth() / WhatsAppVideoCalls.this.videoView.getHeight();
                WhatsAppVideoCalls.this.surfaceView.getHeight();
                WhatsAppVideoCalls.this.surfaceView2.getHeight();
                mediaPlayer.setLooping(true);
                float f = videoWidth / width;
            }
        });
        this.handler = new Handler();
        this.calling = findViewById(R.id.txtcall);
        this.nameuser = findViewById(R.id.txtname);
        this.circleImageView = findViewById(R.id.imguser);
        if (Constant.CHAR_BITMAP != null) {
            this.circleImageView.setImageBitmap(Constant.CHAR_BITMAP);
        } else {
            this.circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_splach_new));
        }

        ImageView imageView = findViewById(R.id.adduser);
        this.adduser = imageView;
        imageView.setVisibility(View.INVISIBLE);
        RelativeLayout relativeLayout = findViewById(R.id.layclose2);
        this.cancel = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "end2";
                WhatsAppVideoCalls.this.mp.stop();

                AppLovinInterstitial();
            }
        });
        RelativeLayout relativeLayout2 = findViewById(R.id.laypesan);
        this.pesan = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatsAppVideoCalls.this.startActivity(new Intent(WhatsAppVideoCalls.this, Home.class));
                WhatsAppVideoCalls.this.finish();
                WhatsAppVideoCalls.this.mp.stop();

            }
        });
        RelativeLayout relativeLayout3 = findViewById(R.id.layclose);
        this.tolak = relativeLayout3;
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "end1";
                WhatsAppVideoCalls.this.mp.stop();


                AppLovinInterstitial();
            }
        });
        RelativeLayout relativeLayout4 = findViewById(R.id.layterima);
        this.terima = relativeLayout4;
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatsAppVideoCalls.this.mp.stop();
                WhatsAppVideoCalls.this.mp.stop();
                WhatsAppVideoCalls.this.calling.setVisibility(View.GONE);
                WhatsAppVideoCalls.this.nameuser.setVisibility(View.GONE);
                WhatsAppVideoCalls.this.circleImageView.setVisibility(View.GONE);
                WhatsAppVideoCalls.this.adduser.setVisibility(View.VISIBLE);
                WhatsAppVideoCalls.this.surfaceView.setVisibility(View.GONE);
                WhatsAppVideoCalls.this.surfaceView2.setVisibility(View.VISIBLE);
                WhatsAppVideoCalls wAVideoCallActivity = WhatsAppVideoCalls.this;
                wAVideoCallActivity.surfaceHolder = wAVideoCallActivity.surfaceView2.getHolder();
                WhatsAppVideoCalls.this.surfaceHolder.addCallback(WhatsAppVideoCalls.this);
                WhatsAppVideoCalls.this.surfaceHolder.setFormat(-1);
                WhatsAppVideoCalls.this.surfaceHolder.setType(View.VISIBLE);
                WhatsAppVideoCalls.this.videoView.start();
                WhatsAppVideoCalls.this.atas.setVisibility(View.GONE);
                WhatsAppVideoCalls.this.bawah.setVisibility(View.VISIBLE);
                WhatsAppVideoCalls.this.tolak.setVisibility(View.VISIBLE);
            }
        });
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Camera open = Camera.open(1);
        this.camera = open;
        this.camera.setParameters(open.getParameters());
        this.camera.setDisplayOrientation(90);
        try {
            this.camera.setPreviewDisplay(surfaceHolder);
            this.camera.startPreview();
        } catch (Exception unused) {
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.camera.stopPreview();
        this.camera.release();
        this.camera = null;
    }

    private void closeActivityWed() {
        WhatsAppVideoCalls.this.startActivity(new Intent(WhatsAppVideoCalls.this, Home.class));
        WhatsAppVideoCalls.this.finish();
        WhatsAppVideoCalls.this.mp.stop();
    }

    private void startActivity(){
        if(string.contains("end1")){
            closeActivityWed();
        }
        else if(string.contains("end2")){
            WhatsAppVideoCalls.this.startActivity(new Intent(WhatsAppVideoCalls.this, Home.class));
            WhatsAppVideoCalls.this.finish();
        }
    }


    private void AppLovinInterstitial() {

            startActivity();
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

                }
            },2000);
        }


    private void BackPress(){
        startActivity(new Intent(this, SelectVideoCall.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        this.mp.stop();
        FBInterstitial();
    }

}