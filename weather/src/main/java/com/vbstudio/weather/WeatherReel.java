package com.vbstudio.weather;

import android.app.Application;
import android.util.Log;

import com.vbstudio.weather.fragment.BaseFragment;
import com.vbstudio.weather.preferences.WeatherPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class WeatherReel extends Application {

    public WeatherReel() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /***********ADD CUSTOM FONTS TO APP**************/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getResources().getString(R.string.custom_font_path))
                .setFontAttrId(R.attr.fontPath)
                .build());
        /***********************************************/
    }

    @Override
    public void onTrimMemory(final int level) {
        super.onTrimMemory(level);

        if (!WeatherPreferences.getAppBackgroundPauseState(getApplicationContext())) {
            Log.i(BaseFragment.LOG_TAG, "APP WENT TO BACKGROUND");

            WeatherPreferences.saveAppBackgroundPauseState(getApplicationContext(), true);
            //BaseFragment.logIntercomEvent(IntercomEvents.INTERCOM_EVENT_APPLICATION_BACKGROUND, null);
        }
    }

}