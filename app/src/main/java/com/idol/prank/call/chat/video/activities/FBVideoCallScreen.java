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

import com.idol.prank.call.chat.video.AdsModule.TinyDBs;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.utils.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

public class FBVideoCallScreen extends AppCompatActivity  implements SurfaceHolder.Callback {


    String string = "";
    private TinyDBs tinyDB;

    LinearLayout atas;
    LinearLayout bawah;
    TextView calling;
    Camera camera;
    ImageView gambrB;
    CircleImageView gambrH;
    Handler handler;
    ImageView imgback;
    MediaPlayer mp;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    RelativeLayout terima;
    RelativeLayout tolak;
    RelativeLayout tolak2;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(1140850688);
        setContentView(R.layout.activity_face_book_video_call_screen);

        tinyDB = new TinyDBs(this);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
        }

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        this.surfaceView = surfaceView;
        surfaceView.setVisibility(View.GONE);
        SurfaceHolder holder = this.surfaceView.getHolder();
        this.surfaceHolder = holder;
        holder.addCallback(this);
        this.surfaceHolder.setFormat(-1);
        this.surfaceHolder.setType(0);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        this.videoView = videoView;
        videoView.setMediaController(null);
        this.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rainbow_video));
        this.videoView.requestFocus();

        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                float videoWidth = mediaPlayer.getVideoWidth() / mediaPlayer.getVideoHeight();
                float width = FBVideoCallScreen.this.videoView.getWidth() / FBVideoCallScreen.this.videoView.getHeight();
                FBVideoCallScreen.this.surfaceView.getHeight();
                mediaPlayer.setLooping(true);

            }
        });
        this.handler = new Handler();
        this.atas = findViewById(R.id.layutama);
        this.bawah = findViewById(R.id.laybawah2);
        this.calling = (TextView) findViewById(R.id.txtwaktu);
        MediaPlayer create = MediaPlayer.create(FBVideoCallScreen.this, RingtoneManager.getDefaultUri(R.raw.facebook_tune));
        this.mp = create;
        create.start();
        this.mp.setLooping(true);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.laytolak);
        this.tolak = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "end1";
                FBVideoCallScreen.this.mp.stop();
                AppLovinInterstitial();
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.imgback2);
        this.imgback = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FBVideoCallScreen.this.startActivity(new Intent(FBVideoCallScreen.this, Home.class));
                FBVideoCallScreen.this.finish();
                FBVideoCallScreen.this.mp.stop();
            }
        });
        RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.laytolak2);
        this.tolak2 = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "end2";
                FBVideoCallScreen.this.mp.stop();
                AppLovinInterstitial();
            }
        });
        RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.layterima);
        this.terima = relativeLayout3;
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FBVideoCallScreen.this.mp.stop();
                FBVideoCallScreen.this.surfaceView.setVisibility(View.VISIBLE);
                FBVideoCallScreen.this.atas.setVisibility(View.GONE);
                FBVideoCallScreen.this.bawah.setVisibility(View.VISIBLE);
                FBVideoCallScreen.this.gambrB.setVisibility(View.GONE);
                FBVideoCallScreen.this.videoView.start();
            }
        });
        //((TextView) findViewById(R.id.txtfbname)).setText(FakeAdapter.judul);
        this.gambrH = (CircleImageView) findViewById(R.id.fbimguser);
        this.gambrB = (ImageView) findViewById(R.id.imgback);

        if (Constant.CHAR_BITMAP != null) {
            this.gambrH.setImageBitmap(Constant.CHAR_BITMAP);
            gambrB.setImageBitmap(Constant.MAIN_CHAR_BITMAP);
        } else {
            this.gambrH.setImageDrawable(getResources().getDrawable(R.drawable.icon_splach_new));
            //imguser2.setImageDrawable(getResources().getDrawable(R.drawable.char1));
        }

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

    private void startActivity(){
        if(string.contains("end1")){
            FBVideoCallScreen.this.startActivity(new Intent(FBVideoCallScreen.this, Home.class));
            FBVideoCallScreen.this.finish();

        }
        else if(string.contains("end2")){
            FBVideoCallScreen.this.startActivity(new Intent(FBVideoCallScreen.this, Home.class));
            FBVideoCallScreen.this.finish();
        }
    }


    private void AppLovinInterstitial() {

            startActivity();

    }

    @Override
    public void onBackPressed() {
        this.mp.stop();
        startActivity(new Intent(this, SelectVideoCall.class));
        finish();
    }

}