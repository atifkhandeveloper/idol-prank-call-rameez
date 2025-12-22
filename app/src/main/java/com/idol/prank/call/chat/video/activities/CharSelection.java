package com.idol.prank.call.chat.video.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.idol.prank.call.chat.video.R;

import com.idol.prank.call.chat.video.utils.Constant;

public class CharSelection extends AppCompatActivity {

    String str;
    private int retry = 0;

    private Button back, next;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        findViewById(R.id.char1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "selected";
                Constant.character_no = 1;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.one);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
                Intent intent = new Intent(getApplicationContext(), CombineMakeCallActivity.class);
                intent.putExtra("str", "selected");
                // Start the activity
                startActivity(intent);
            }
        });

        findViewById(R.id.char2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "selected";
                Constant.character_no = 2;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.two);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
                Intent intent = new Intent(getApplicationContext(), CombineMakeCallActivity.class);
                intent.putExtra("str", "selected");
                // Start the activity
                startActivity(intent);
            }
        });

        findViewById(R.id.char3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "selected";
                Constant.character_no = 3;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.three);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
                Intent intent = new Intent(getApplicationContext(), CombineMakeCallActivity.class);
                intent.putExtra("str", "selected");
                // Start the activity
                startActivity(intent);
            }
        });

        findViewById(R.id.char4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.character_no = 4;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.four);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
                Intent intent = new Intent(getApplicationContext(), CombineMakeCallActivity.class);
                intent.putExtra("str", "selected");
                // Start the activity
                startActivity(intent);
            }
        });

        findViewById(R.id.char5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.character_no = 5;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.five);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
                Intent intent = new Intent(getApplicationContext(), CombineMakeCallActivity.class);
                intent.putExtra("str", "selected");
                // Start the activity
                startActivity(intent);
            }
        });

        findViewById(R.id.char6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.character_no = 6;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.six);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
                Intent intent = new Intent(getApplicationContext(), CombineMakeCallActivity.class);
                intent.putExtra("str", "selected");
                // Start the activity
                startActivity(intent);
            }
        });

        findViewById(R.id.btc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(CharSelection.this , Home.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.isNetworkAvailable(CharSelection.this)) {
            ProgressDialog progress = new ProgressDialog(CharSelection.this);
            progress.setTitle("Alert");
            progress.setMessage("Please wait...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.native_ad_frame_Char1).setVisibility(View.VISIBLE);
                    findViewById(R.id.native_ad_frame_Char2).setVisibility(View.VISIBLE);
                    findViewById(R.id.native_ad_frame_Char3).setVisibility(View.VISIBLE);
                    if (!isFinishing()) {
                        progress.dismiss();
                    }
                }
            }, 4000);

        }
    }

    @Override
    public void onBackPressed() {


            startActivity(new Intent(CharSelection.this , Home.class));


    }

}