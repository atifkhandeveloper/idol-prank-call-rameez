package com.idol.prank.call.chat.video.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.idol.prank.call.chat.video.R;

import com.idol.prank.call.chat.video.activities.fragments.LiveChat;
import com.idol.prank.call.chat.video.utils.Constant;


public class Home extends AppCompatActivity {
    Boolean checked = false;
    RelativeLayout voice_call_button, video_call_button, characterSelect, menu, chat;
    ImageView settings;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    private static final int MY_REQUEST_CODE = 17326;
    String str = null;
    private int retry = 0;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        takePermission();

        chat = findViewById(R.id.btn_message);
        voice_call_button = findViewById(R.id.btn_audio_call);
        video_call_button = findViewById(R.id.btn_video_call);
        settings = findViewById(R.id.iv_settings);
//        menu = findViewById(R.id.set);
//        characterSelect = findViewById(R.id.characterSelect);


        voice_call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Home.this, CharSelection.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(Home.this, LiveChat.class));

            }
        });

        video_call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(Home.this, CharSelection.class));
                }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(Home.this, SettingOption.class));

            }
        });

//        characterSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    startActivity(new Intent(Home.this, CharSelection.class));
//
//            }
//        });
    }



    private void takePermission() {
        if (Build.VERSION.SDK_INT >= 28 && !Settings.canDrawOverlays(this)) {
            checkPermissionWed();
        }
        if (Build.VERSION.SDK_INT < 26) {
            new WindowManager.LayoutParams(-2, -2, 2002, 40, -2);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.CAMERA"}, 1);
        }
    }

    public void checkPermissionWed() {
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            return;
        }
        startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE && !Settings.canDrawOverlays(this)) {
            checkPermissionWed();
        }
        if (i == MY_REQUEST_CODE) {
            if (i2 == -1) {
                if (i2 == -1) {
                    return;
                }
                Log.d("RESULT_OK  :", "" + i2);
            } else if (i2 == 0) {
                if (i2 == 0) {
                    return;
                }
                Log.d("RESULT_CANCELED  :", "" + i2);
            } else if (i2 != 1 || i2 == 1) {
            } else {
                Log.d("RESULT_IN_APP_FAILED:", "" + i2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checked) {

        } else {
            if (Constant.isNetworkAvailable(Home.this)) {
                ProgressDialog progress = new ProgressDialog(Home.this);
                progress.setTitle("Alert");
                progress.setMessage("Please wait...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.native_ad_frame_Main).setVisibility(View.VISIBLE);
                        if (!isFinishing()) {
                            progress.dismiss();
                        }
                    }
                }, 4000);

            }
        }

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}