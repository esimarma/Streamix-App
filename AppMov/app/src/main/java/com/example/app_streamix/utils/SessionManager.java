package com.example.app_streamix.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AppStreamixPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_MOVIE_WASTED_TIME = "userMovieWastedTime";
    private static final String KEY_USER_SERIES_WASTED_TIME = "userSeriesWastedTime";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserData(Long id,String name, String email, Integer movieWastedTime, Integer seriesWastedTime) {
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putLong(KEY_USER_ID, id);
        editor.putInt(KEY_USER_MOVIE_WASTED_TIME, movieWastedTime);
        editor.putInt(KEY_USER_SERIES_WASTED_TIME, seriesWastedTime);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "utilizador");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "Email não disponível");
    }
    public Long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, 0);
    }
    public Integer getUserMovieWastedTime() {
        return sharedPreferences.getInt(KEY_USER_MOVIE_WASTED_TIME, 0);
    }
    public Integer getUserSeriesWastedTime() {
        return sharedPreferences.getInt(KEY_USER_SERIES_WASTED_TIME, 0);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}