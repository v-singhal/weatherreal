package com.vbstudio.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.vbstudio.weather.fragment.BaseFragment;
import com.vbstudio.weather.network.NetworkManager;
import com.vbstudio.weather.preferences.WeatherPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    public static final int MAIN_ACTIVITY_CONTAINER_ID = R.id.container;

    protected Toolbar toolbar;
    protected NetworkManager networkManager;
    //protected SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sqliteHelper = new SqliteHelper(this);
        //networkManager = NetworkManager.newInstance(this.getBaseContext(), BaseFragment.BASE_URL);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onResume() {
        super.onResume();

        Boolean isAppComingFromBackground = WeatherPreferences.getAppBackgroundPauseState(this);

        if (isAppComingFromBackground) {
            Log.i(BaseFragment.LOG_TAG, "APP COMING TO FOREGROUND");

            //BaseFragment.logIntercomEvent(IntercomEvents.INTERCOM_EVENT_APPLICATION_FOREGROUND, null);
            //BaseFragment.updateUserInfoToIntercom(this);
            WeatherPreferences.saveAppBackgroundPauseState(this, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String title = "";
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            title = fragment.getTitle();
        }
        setTitle(title);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView titleView = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.title);
        titleView.setText(title);
    }

    protected void configureToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setCustomView(R.layout.toolbar_view);
    }

    public BaseFragment getBaseFragmentInContainer() {
        String activityName = this.getClass().getSimpleName();

        Log.i(BaseFragment.LOG_TAG, activityName);

        return BaseFragment.getFragmentFromCurrentActivity(this);
    }
}