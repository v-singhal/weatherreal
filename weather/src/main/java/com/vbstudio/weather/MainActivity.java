package com.vbstudio.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vbstudio.weather.fragment.BaseFragment;
import com.vbstudio.weather.preferences.WeatherPreferences;


public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle drawerToggle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceListener;
    private boolean isSharedPreferenceListenerRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        openAptFragment();
    }

    private void openAptFragment() {
        BaseFragment fragment = new BaseFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        return;
    }

    @Override
    public void onResume() {
        super.onResume();

        registerOrobindPreferenceListener();
    }

    private void registerOrobindPreferenceListener() {
        if (!isSharedPreferenceListenerRegistered) {
            registerSharedPreferenceListener();
        }
    }

    private void registerSharedPreferenceListener() {
        sharedPreferences = WeatherPreferences.getPreferences(this);
        sharedPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            /**
             * Called when a shared preference is changed, added, or removed. This
             * may be called even if a preference is set to its existing value.
             * <p/>
             * <p>This callback will be run on your main thread.
             *
             * @param sharedPreferences The {@link android.content.SharedPreferences} that received
             *                          the change.
             * @param key               The key of the preference that was changed, added, or
             */
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(WeatherPreferences.IS_APP_IN_BACKGROUND)) {
                }
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceListener);
        isSharedPreferenceListenerRegistered = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener);
        isSharedPreferenceListenerRegistered = false;
    }

    private void configureToolbar() {
        super.configureToolbar((Toolbar) findViewById(R.id.appToolbar));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.closed_drawer);
    }
}
