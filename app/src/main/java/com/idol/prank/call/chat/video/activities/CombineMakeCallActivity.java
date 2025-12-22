package com.idol.prank.call.chat.video.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.receiver.ReceiveCalls;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.Calendar;

public class CombineMakeCallActivity extends AppCompatActivity {
    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private RadioGroup list_template;
    private RadioGroup list_time;
    private RadioGroup list_calls;
    TextView tittle, callType, callType_2;
    ImageView image_type, image_type_2;
    CardView callnowcard;

    private ProgressDialog progressDialog;
    private int retry = 0;

    private String str = null;
    private Boolean checked = false;
    private PendingIntent pendingIntent;
    RadioButton audioradiobtn, videoradiobtn;
    private static final int ALARM_REQUEST_CODE = 134;
    ImageView backicon;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_make_call);

        radioGroup1 = findViewById(R.id.list_Template);
        list_template = radioGroup1;

        radioGroup2 = findViewById(R.id.list_calls);
        list_calls = radioGroup2;

        radioGroup3 = findViewById(R.id.list_time);
        list_time = radioGroup3;
        callnowcard = findViewById(R.id.startcallcard);

        audioradiobtn = findViewById(R.id.radio_whatsapp_buttoncal);
        videoradiobtn = findViewById(R.id.radio_facebook_buttoncal);
        backicon = findViewById(R.id.backicon);

        showProgressDialog();

        findIdsWed();
        checkingCallTypeWed();




        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CombineMakeCallActivity.this , CharSelection.class));

            }
        });


        audioradiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        videoradiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_whatsapp_buttoncal:
                        str = "Audio";
                        ApplovinBtns();
                        break;
                    case R.id.radio_facebook_buttoncal:
                        str = "Video";
                        ApplovinBtns1();
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
                        SelectCall.rd_time = 1;
                        SelectCall.status_time = "Wait for 2 seconds";
                        return;
                    case R.id.radio_10:
                        SelectCall.rd_time = Constant.TIMER_A;
                        SelectCall.status_time = "Wait for 10 seconds";
                        return;
                    case R.id.radio_30:
                        SelectCall.rd_time = Constant.TIMER_B;
                        SelectCall.status_time = "Wait for 30 seconds";
                        return;
                    case R.id.radio_60:
                        SelectCall.rd_time = Constant.TIMER_C;
                        SelectCall.status_time = "Wait for 1 minutes";
                        return;
                    default:
                        return;
                }
            }
        });
        this.pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, new Intent(this, ReceiveCalls.class), PendingIntent.FLAG_IMMUTABLE);

        callnowcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "voicecall";
                checked = true;
                if (radioGroup1.getCheckedRadioButtonId() == -1 && radioGroup3.getCheckedRadioButtonId() == -1 && radioGroup2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CombineMakeCallActivity.this, "Please select Template & Timer", Toast.LENGTH_SHORT).show();
                }
                if (radioGroup2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CombineMakeCallActivity.this, "Please select call Format", Toast.LENGTH_SHORT).show();
                } else if (radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CombineMakeCallActivity.this, "Please select Timer", Toast.LENGTH_SHORT).show();
                } else {

                        ApplovinBtns1();

                }

            }
        });


    }

    private void ApplovinBtns1() {
        if (checked == true) {
            if (str.contains("Audio")) {
                SelectCall.rd_form = 2;
            } else if (str.contains("Video")) {
                SelectCall.rd_form = 1;
            } else if (str.contains("voicecall")) {
                if (radioGroup1.getCheckedRadioButtonId() != -1 && radioGroup3.getCheckedRadioButtonId() != -1 && radioGroup2.getCheckedRadioButtonId() != -1) {
                    if (SelectCall.rd_time == 1) {
                        if (SelectCall.rd_form == 1) {
                            if (SelectCall.rd_vid == 2) {
                                Intent intent = new Intent(CombineMakeCallActivity.this, WhatsAppVideoCalls.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent);
                                CombineMakeCallActivity.this.finish();
                                Log.d("CHK1", "onClick: " + "voice call");
                                return;
                            } else if (SelectCall.rd_vid == 1) {
                                Log.d("CHK1", "onClick: " + "video call");
                                Intent intent2 = new Intent(CombineMakeCallActivity.this, WhatsAppVideoCalls.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent2);
                                CombineMakeCallActivity.this.finish();
                                return;
                            } else {
                                Intent intent3 = new Intent(CombineMakeCallActivity.this, WhatsAppVideoCalls.class);
                                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent3);
                                CombineMakeCallActivity.this.finish();
                                return;
                            }
                        } else if (SelectCall.rd_form == 2) {
                            if (SelectCall.rd_vid == 2) {
                                Intent intent4 = new Intent(CombineMakeCallActivity.this, FBVoiceCallScreen.class);
                                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent4);
                                CombineMakeCallActivity.this.finish();
                                return;
                            } else if (SelectCall.rd_vid == 1) {
                                Intent intent5 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                                intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent5);
                                CombineMakeCallActivity.this.finish();
                                return;
                            } else {
                                Intent intent6 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                                intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent6);
                                CombineMakeCallActivity.this.finish();
                                return;
                            }
                        } else if (SelectCall.rd_form != 3) {
                            return;
                        } else {
                            if (SelectCall.rd_vid == 2) {
                                Intent intent7 = new Intent(CombineMakeCallActivity.this, CallSystem.class);
                                intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent7);
                                CombineMakeCallActivity.this.finish();
                                return;
                            } else if (SelectCall.rd_vid == 1) {
                                Intent intent8 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                                intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent8);
                                CombineMakeCallActivity.this.finish();
                                return;
                            } else {
                                Intent intent9 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                                intent9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                CombineMakeCallActivity.this.startActivity(intent9);
                                CombineMakeCallActivity.this.finish();
                                return;
                            }
                        }
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(13, SelectCall.rd_time);
                    //((AlarmManager) MainActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, calendar.getTimeInMillis(), MainActivity.this.pendingIntent);
                    ((AlarmManager) CombineMakeCallActivity.this.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), CombineMakeCallActivity.this.pendingIntent);
                    Toast.makeText(CombineMakeCallActivity.this, SelectCall.status_time, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    CombineMakeCallActivity.this.finish();
                } else {
                    if (radioGroup1.getCheckedRadioButtonId() == -1 && radioGroup3.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(CombineMakeCallActivity.this, "Please select Template & Timer", Toast.LENGTH_SHORT).show();
                    } else if (radioGroup1.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(CombineMakeCallActivity.this, "Please select Template", Toast.LENGTH_SHORT).show();
                    } else if (radioGroup2.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(CombineMakeCallActivity.this, "Please select call Formate", Toast.LENGTH_SHORT).show();
                    } else if (radioGroup3.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(CombineMakeCallActivity.this, "Please select Timer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            checked = false;
        } else {
            ApplovinBtns();
            checked = true;
        }

    }


    private void ApplovinBtns() {
        if (str.contains("facebook")) {
            SelectCall.rd_form = 2;
        } else if (str.contains("whatsapp")) {
            SelectCall.rd_form = 1;
        } else if (str.contains("voicecall")) {
            if (radioGroup2.getCheckedRadioButtonId() != -1 && radioGroup3.getCheckedRadioButtonId() != -1) {
                if (SelectCall.rd_time == 1) {
                    if (SelectCall.rd_form == 1) {
                        if (SelectCall.rd_vid == 2) {
                            Intent intent = new Intent(CombineMakeCallActivity.this, WhatsAppCalls.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent);
                            CombineMakeCallActivity.this.finish();
                            Log.d("CHK1", "onClick: " + "voice call");
                            return;
                        } else if (SelectCall.rd_vid == 1) {
                            Log.d("CHK1", "onClick: " + "video call");
                            Intent intent2 = new Intent(CombineMakeCallActivity.this, WhatsAppVideoCalls.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent2);
                            CombineMakeCallActivity.this.finish();
                            return;
                        } else {
                            Intent intent3 = new Intent(CombineMakeCallActivity.this, WhatsAppVideoCalls.class);
                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent3);
                            CombineMakeCallActivity.this.finish();
                            return;
                        }
                    } else if (SelectCall.rd_form == 2) {
                        if (SelectCall.rd_vid == 2) {
                            Intent intent4 = new Intent(CombineMakeCallActivity.this, FBVoiceCallScreen.class);
                            intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent4);
                            CombineMakeCallActivity.this.finish();
                            return;
                        } else if (SelectCall.rd_vid == 1) {
                            Intent intent5 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                            intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent5);
                            CombineMakeCallActivity.this.finish();
                            return;
                        } else {
                            Intent intent6 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                            intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent6);
                            CombineMakeCallActivity.this.finish();
                            return;
                        }
                    } else if (SelectCall.rd_form != 3) {
                        return;
                    } else {
                        if (SelectCall.rd_vid == 2) {
                            Intent intent7 = new Intent(CombineMakeCallActivity.this, CallSystem.class);
                            intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent7);
                            CombineMakeCallActivity.this.finish();
                            return;
                        } else if (SelectCall.rd_vid == 1) {
                            Intent intent8 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                            intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent8);
                            CombineMakeCallActivity.this.finish();
                            return;
                        } else {
                            Intent intent9 = new Intent(CombineMakeCallActivity.this, FBVideoCallScreen.class);
                            intent9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            CombineMakeCallActivity.this.startActivity(intent9);
                            CombineMakeCallActivity.this.finish();
                            return;
                        }
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(13, SelectCall.rd_time);
                //((AlarmManager) MainActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, calendar.getTimeInMillis(), MainActivity.this.pendingIntent);
                ((AlarmManager) CombineMakeCallActivity.this.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), CombineMakeCallActivity.this.pendingIntent);
                Toast.makeText(CombineMakeCallActivity.this, SelectCall.status_time, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                CombineMakeCallActivity.this.finish();
            } else {
                if (radioGroup2.getCheckedRadioButtonId() == -1 && radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CombineMakeCallActivity.this, "Please select Template & Timer", Toast.LENGTH_SHORT).show();
                } else if (radioGroup2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CombineMakeCallActivity.this, "Please select Template", Toast.LENGTH_SHORT).show();
                } else if (radioGroup3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CombineMakeCallActivity.this, "Please select Timer", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkingCallTypeWed() {


        if (Constant.IS_VIDEO) {
            Constant.IS_VIDEO = false;
            SelectCall.rd_vid = 1;

        } else if (Constant.IS_VOICE) {
            Constant.IS_VOICE = false;
            SelectCall.rd_vid = 2;

        } else {
        }

    }

    public void findIdsWed() {

        callType_2 = findViewById(R.id.selected_call_text);
        tittle = (TextView) findViewById(R.id.text_Tittle);
        image_type_2 = findViewById(R.id.img_type_call);
        radioGroup1 = findViewById(R.id.list_Template);
        list_template = radioGroup1;

        radioGroup2 = findViewById(R.id.list_calls);
        list_calls = radioGroup2;

        radioGroup3 = findViewById(R.id.list_time);
        list_time = radioGroup3;
    }


    private void LoadInterstitalAd() {

                startActivity(new Intent(CombineMakeCallActivity.this , CharSelection.class));
    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(CombineMakeCallActivity.this , CharSelection.class));
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);
    }


}





