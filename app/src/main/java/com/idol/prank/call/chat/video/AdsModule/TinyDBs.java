package com.idol.prank.call.chat.video.AdsModule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.idol.prank.call.chat.video.R;


public class TinyDBs {
    private final SharedPreferences preferences;
    Context activity;
    private final String SELECTED_ID = "selected_id";
    private final String SELECTED_INTERESTITIAL_ID = "selected_inter_id";
    private final String SELECTED_NATIVE_ID = "selected_native_id";
    private final String SELECTED_BANNER_ID = "selected_banner_id";
    public  static Activity activityy;

    public TinyDBs(Context context) {
        this.activity = context;
        preferences = context.getSharedPreferences(Constants.MY_PREFS, Context.MODE_PRIVATE);
    }

    public void setResolution(String filterName, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(filterName, value);
        editor.apply();
    }

    public int getResolution(String filterName) {
        return preferences.getInt(filterName, 45);
    }

    public void setSetOff(String filterName, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(filterName, value);
        editor.apply();
    }

    public int getSetOff(String filterName) {
        return preferences.getInt(filterName, 24);
    }


    public void setSelectedId(int IncVal) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SELECTED_ID, IncVal);
        editor.apply();
    }

    public int getSelectedId() {
        return preferences.getInt(SELECTED_ID, 1);
    }

    public void setInterestitialId(String IncVal) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_INTERESTITIAL_ID, IncVal);
        editor.apply();
    }


    public void setNativeId(String IncVal) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_NATIVE_ID, IncVal);
        editor.apply();
    }



    public void setBannerId(String IncVal) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_BANNER_ID, IncVal);
        editor.apply();
    }


}
