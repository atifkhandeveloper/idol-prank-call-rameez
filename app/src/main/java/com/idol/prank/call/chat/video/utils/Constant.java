package com.idol.prank.call.chat.video.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.idol.prank.call.chat.video.R;


public class Constant {
    public static int TIMER_NOW = 2;
    public static int TIMER_A = 10;
    public static int TIMER_B = 30;
    public static int TIMER_C = 60;
    public static int TIMER_D = 300;

    public static int character_no;

    public static String layouttype = "";


    public static boolean IS_VOICE = false;
    public static boolean IS_VIDEO = false;
    public static boolean IS_SYSTEM = false;

    public static Bitmap CHAR_BITMAP;
    public static Bitmap MAIN_CHAR_BITMAP;

    public String storeValue = "";
    SharedPreferences preferences;

    public Constant(Context context){
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public String getStoreValue() {
        return preferences.getString("save_value", "false");
    }

    public void setStoreValue(String storeValue) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("save_value", storeValue);
        editor.commit();
        //this.storeValue = storeValue;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void shareApp(Context context) throws Exception {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        String sAux = "\nLet me recommend you this application\n\n" + context.getResources().getString(R.string.app_name) + "\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + " \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        context.startActivity(Intent.createChooser(i, "choose one"));
    }


    public static void moreApps(Context context, String account_name) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + account_name)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub:" + account_name)));
        }
    }

    public static void rate(Context context, String app_link) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="  + app_link)));
        } catch (android.content.ActivityNotFoundException ex) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + app_link)));
        }
    }
}
