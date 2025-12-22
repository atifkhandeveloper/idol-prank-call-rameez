package com.idol.prank.call.chat.video.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.idol.prank.call.chat.video.AdsModule.Constants;
import com.idol.prank.call.chat.video.AdsModule.TinyDBs;
import com.idol.prank.call.chat.video.receiver.ReceiveCalls;
import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.Calendar;

public class SelectVideoCall<Int> extends AppCompatActivity {

    private TinyDBs tinyDB;

    private Boolean checked = false;
    private String str = null;
    protected static final String TAG = "SelectCallingOption";
    public static String status_time = "Wait for 2 seconds";
    public static int rd_form = 1;
    public static int rd_time = 1;
    public static int rd_vid = 1;
    private PendingIntent pendingIntent;
    private static final int ALARM_REQUEST_CODE = 134;
    private Button back_button;

    TextView tittle,callType,callType_2;
    ImageView image_type,image_type_2;
    LinearLayout start_call_button,callButton;
        int i=1;
    private int retry = 0;
    private RadioGroup list_template;
    private RadioGroup list_time;
    private RadioButton radio_facebook_btn,radio_system_call_btn;
    RadioGroup radioGroup2,radioGroup3;

    String [] arrCallType = new String[]{"Whatsapp","Facebook"};
    String [] arrSetTimer = new String[]{"Now","10","30","60","300"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calling_optionss);

        tinyDB = new TinyDBs(this);
        // Getting all the ids
        findIdsWed();

        // Back Button Adds loading stuff
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(SelectVideoCall.this , Home.class));

            }
        });

        checkingCallTypeWed();

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_facebook_button:
                        str = "facebook";
                      //  AppLovinInterstitial();
                        break;
                    case R.id.radio_whatsapp_button:
                        str = "whatsapp";
                      //  AppLovinInterstitial();
                        break;
                    default:
                        return;
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_1:
                        SelectVideoCall.rd_time = 1;
                        SelectVideoCall.status_time = "Wait for 2 seconds";
                        return;
                    case R.id.radio_10:
                        SelectVideoCall.rd_time = Constant.TIMER_A;
                        SelectVideoCall.status_time = "Wait for 10 seconds";
                        return;
                    case R.id.radio_30:
                        SelectVideoCall.rd_time = Constant.TIMER_B;
                        SelectVideoCall.status_time = "Wait for 30 seconds";
                        return;
                    case R.id.radio_300:
                        SelectVideoCall.rd_time = Constant.TIMER_D;
                        SelectVideoCall.status_time = "Wait for 5 minutes";
                        return;
                    case R.id.radio_60:
                        SelectVideoCall.rd_time = Constant.TIMER_C;
                        SelectVideoCall.status_time = "Wait for 1 minutes";
                        return;
                    default:
                        return;
                }
            }
        });


        //Passing Intent to next Activity to schedule a call
        this.pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, new Intent(this, ReceiveCalls.class), PendingIntent.FLAG_IMMUTABLE);
        start_call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                str = "videocall";
                checked = true;
                if (radioGroup2.getCheckedRadioButtonId() == -1 && radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SelectVideoCall.this, "Please select Template & Timer", Toast.LENGTH_SHORT).show();
                } else if (radioGroup2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SelectVideoCall.this, "Please select Template", Toast.LENGTH_SHORT).show();
                } else if (radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SelectVideoCall.this, "Please select Timer", Toast.LENGTH_SHORT).show();
                }
                else{

                        AppLovinInterstitial();
                    }


            }
        });

    }

    private void checkingCallTypeWed() {

        if (Constant.IS_VIDEO){
            Constant.IS_VIDEO = false;
            SelectVideoCall.rd_vid = 1;

        } else if (Constant.IS_VOICE) {
            Constant.IS_VOICE = false;
            SelectVideoCall.rd_vid = 2;

        }else {}

    }

    public void findIdsWed() {/*
        callType = findViewById(R.id.call_type);*/
        callType_2 = findViewById(R.id.selected_call_text);
        back_button = findViewById(R.id.back_button);
        start_call_button = findViewById(R.id.start_call_btn);
        tittle = (TextView) findViewById(R.id.text_Tittle);/*
        image_type = findViewById(R.id.calling_image);*/
        image_type_2 = findViewById(R.id.img_type_call);
    /*    rl_native_ad = findViewById(R.id.rl_native_ad);
        rl_native_ad.setVisibility(View.GONE);*/

        radioGroup2 = findViewById(R.id.list_Template);
        list_template = radioGroup2;

        radioGroup3 = findViewById(R.id.list_time);
        list_time = radioGroup3;
    }

    private void CheckForNetwork() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checked){

        }
        else{
            if (Constant.isNetworkAvailable(SelectVideoCall.this)) {
                ProgressDialog progress = new ProgressDialog(SelectVideoCall.this);
                progress.setTitle("Alert");
                progress.setMessage("Please wait...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            progress.dismiss();
                        }
                    }
                }, 4000);

            }
        }

    }

    private void AppLovinInterstitial() {
        if(Constants.isNetworkAvailable(this)) {

            ApplovinBtns();
        }

    }

    private void ApplovinBtns(){

        if(str.contains("facebook")){
            SelectCall.rd_form = 2;
        }
        else if(str.contains("whatsapp")){
            SelectCall.rd_form = 1;
        }
        else if(str.contains("videocall")){
            if (radioGroup2.getCheckedRadioButtonId() != -1 && radioGroup3.getCheckedRadioButtonId() != -1) {
                if (SelectVideoCall.rd_time == 1) {
                    if (SelectVideoCall.rd_form == 1) {
                        if (SelectVideoCall.rd_vid == 2) {
                            Intent intent = new Intent(SelectVideoCall.this, WhatsAppCalls.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent);
                            SelectVideoCall.this.finish();
                            Log.d("CHK1", "onClick: " + "voice call");
                            return;
                        } else if (SelectVideoCall.rd_vid == 1) {
                            Log.d("CHK1", "onClick: " + "video call");
                            Intent intent2 = new Intent(SelectVideoCall.this, WhatsAppVideoCalls.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent2);
                            SelectVideoCall.this.finish();
                            return;
                        } else {
                            Intent intent3 = new Intent(SelectVideoCall.this, WhatsAppVideoCalls.class);
                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent3);
                            SelectVideoCall.this.finish();
                            return;
                        }
                    } else if (SelectVideoCall.rd_form == 2) {
                        if (SelectVideoCall.rd_vid == 2) {
                            Intent intent4 = new Intent(SelectVideoCall.this, FBVoiceCallScreen.class);
                            intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent4);
                            SelectVideoCall.this.finish();
                            return;
                        } else if (SelectVideoCall.rd_vid == 1) {
                            Intent intent5 = new Intent(SelectVideoCall.this, FBVideoCallScreen.class);
                            intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent5);
                            SelectVideoCall.this.finish();
                            return;
                        } else {
                            Intent intent6 = new Intent(SelectVideoCall.this, FBVideoCallScreen.class);
                            intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent6);
                            SelectVideoCall.this.finish();
                            return;
                        }
                    } else if (SelectVideoCall.rd_form != 3) {
                        return;
                    } else {
                        if (SelectVideoCall.rd_vid == 2) {
                            Intent intent7 = new Intent(SelectVideoCall.this, CallSystem.class);
                            intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent7);
                            SelectVideoCall.this.finish();
                            return;
                        } else if (SelectVideoCall.rd_vid == 1) {
                            Intent intent8 = new Intent(SelectVideoCall.this, FBVideoCallScreen.class);
                            intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent8);
                            SelectVideoCall.this.finish();
                            return;
                        } else {
                            Intent intent9 = new Intent(SelectVideoCall.this, FBVideoCallScreen.class);
                            intent9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SelectVideoCall.this.startActivity(intent9);
                            SelectVideoCall.this.finish();
                            return;
                        }
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(13, SelectVideoCall.rd_time);
                //((AlarmManager) MainActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, calendar.getTimeInMillis(), MainActivity.this.pendingIntent);
                ((AlarmManager) SelectVideoCall.this.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), SelectVideoCall.this.pendingIntent);
                Toast.makeText(SelectVideoCall.this, SelectVideoCall.status_time, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                SelectVideoCall.this.finish();
            } else {
                if (radioGroup2.getCheckedRadioButtonId() == -1 && radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SelectVideoCall.this, "Please select Template & Timer", Toast.LENGTH_SHORT).show();
                } else if (radioGroup2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SelectVideoCall.this, "Please select Template", Toast.LENGTH_SHORT).show();
                } else if (radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SelectVideoCall.this, "Please select Timer", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void BackPress(){
        startActivity(new Intent(this,Home.class));
        finish();
    }

    @Override
    public void onBackPressed() {

            startActivity(new Intent(SelectVideoCall.this , Home.class));

    }

}