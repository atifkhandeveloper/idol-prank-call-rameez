package com.idol.prank.call.chat.video.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref mSharedPref;
    private final SharedPreferences sharedPreferences;
    public String storeValue = "";
    SharedPreferences preferences;
    public static SharedPref getInstance(Context context) {
        if (mSharedPref == null) {
            mSharedPref = new SharedPref(context);
        }
        return mSharedPref;
    }
    public SharedPref(Context context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("prank_call", Context.MODE_PRIVATE);
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

    public void isPolicyRead(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getPolicyRead(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }


}
