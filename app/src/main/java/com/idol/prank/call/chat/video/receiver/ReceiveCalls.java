package com.idol.prank.call.chat.video.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.idol.prank.call.chat.video.activities.FBVideoCallScreen;
import com.idol.prank.call.chat.video.activities.FBVoiceCallScreen;
import com.idol.prank.call.chat.video.activities.SelectCall;
import com.idol.prank.call.chat.video.activities.CallSystem;
import com.idol.prank.call.chat.video.activities.WhatsAppVideoCalls;
import com.idol.prank.call.chat.video.activities.WhatsAppCalls;

public class ReceiveCalls extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SelectCall.rd_form == 1) {
            if (SelectCall.rd_vid == 2) {
                Intent intent2 = new Intent(context, WhatsAppCalls.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            } else if (SelectCall.rd_vid == 1) {
                Intent intent3 = new Intent(context, WhatsAppVideoCalls.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent3);
            } else {
                Intent intent4 = new Intent(context, WhatsAppVideoCalls.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent4);
            }
        } else if (SelectCall.rd_form == 2) {
            if (SelectCall.rd_vid == 2) {
                Intent intent5 = new Intent(context, FBVoiceCallScreen.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent5);
            } else if (SelectCall.rd_vid == 1) {
                Intent intent6 = new Intent(context, FBVideoCallScreen.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent6);
            } else {
                Intent intent7 = new Intent(context, FBVideoCallScreen.class);
                intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent7);
            }
        } else if (SelectCall.rd_form != 3) {
        } else {
            if (SelectCall.rd_vid == 2) {
                Intent intent8 = new Intent(context, CallSystem.class);
                intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent8);
            } else if (SelectCall.rd_vid == 1) {
               /* Intent intent9 = new Intent(context, TeleVideoCallActivity.class);
                intent9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent9);*/
            } else {
              /*  Intent intent10 = new Intent(context, TeleVideoCallActivity.class);
                intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent10);*/
            }
        }
    }
}
