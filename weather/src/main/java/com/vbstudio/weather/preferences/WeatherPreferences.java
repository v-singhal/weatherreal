package com.vbstudio.weather.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class WeatherPreferences {

    public static final String IS_APP_IN_BACKGROUND = "isAppInBackground";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("weatherPreferences", Context.MODE_PRIVATE);
    }

    public static void clearAll(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    public static Boolean getAppBackgroundPauseState(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getBoolean(IS_APP_IN_BACKGROUND, false);
    }

    public static void saveAppBackgroundPauseState(Context context, Boolean isMainPaused) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putBoolean(IS_APP_IN_BACKGROUND, isMainPaused).commit();
    }
}
