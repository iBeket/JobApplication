package com.example.milos.creitiveblogapp;

import android.content.Context;
import android.content.SharedPreferences;
//class containing getters and setters where we are going to store token

public class SessionClass {

    public String getToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("com.milos.myapplication.SESSIONDATA", context.MODE_PRIVATE);
        return sharedPrefs.getString("token", "");
    }

    public void setToken(String token, Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("com.milos.myapplication.SESSIONDATA", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
