package com.idol.prank.call.chat.video.AdsModule;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.FrameLayout;

import com.idol.prank.call.chat.video.R;

public class Constants {

    public static FrameLayout frameLayout;
    public static FrameLayout nativeframe;


    public static Bitmap CHAR_BITMAP = null;

    public static final String EXTRA_CHAR_NAME = "extra_char_name";

    public static String layouttype = "";
    public static final String MY_PREFS = "traffic_prefs";
    public  static Activity activitymain;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setIds(Context context) {
        TinyDBs tinyDBs = new TinyDBs(context);
        Log.d("addds", "setIds: " + tinyDBs.getSelectedId());
        switch (tinyDBs.getSelectedId()) {
            case 1:
                tinyDBs.setSelectedId(2);

                break;
            case 2:
                tinyDBs.setSelectedId(3);

                break;
            case 3:
                tinyDBs.setSelectedId(4);

                break;
            case 4:
                tinyDBs.setSelectedId(5);

                break;
            case 5:
                tinyDBs.setSelectedId(1);

                break;


        }
    }

}
