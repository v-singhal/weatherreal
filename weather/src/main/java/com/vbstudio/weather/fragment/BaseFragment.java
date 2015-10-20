package com.vbstudio.weather.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.vbstudio.weather.BaseActivity;
import com.vbstudio.weather.MainActivity;
import com.vbstudio.weather.R;
import com.vbstudio.weather.network.NetworkManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.vbstudio.weather.utils.KeyboardUtils.hideKeyboard;
import static com.vbstudio.weather.utils.StringUtils.isValidString;
import static com.vbstudio.weather.utils.UIUtils.animateFadeHide;
import static com.vbstudio.weather.utils.UIUtils.animateFadeShow;
import static com.vbstudio.weather.utils.UIUtils.animateImageAddition;

public class BaseFragment extends DialogFragment implements /*Response.Listener<JSONObject>, Response.ErrorListener,*/ View.OnClickListener {

    public final static String LOG_TAG = "WEATHER";

    private String title;
    private NetworkManager networkManager;

    public static void addToBackStack(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        Log.i(BaseFragment.LOG_TAG, "FM SIZE IN ADD BEFORE: " + fragmentManager.getBackStackEntryCount());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();

        Log.i(BaseFragment.LOG_TAG, "FM SIZE IN ADD AFTER: " + fragmentManager.getBackStackEntryCount());
    }

    public static void replaceStack(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(R.id.container, fragment).commit();
    }

    public static void openAniamtedDialog(Dialog dialog) {
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();
    }

    public static Typeface customTypefaceFromBase(Context context) {
        return Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.custom_font_path));
    }

    public static BaseFragment getFragmentFromCurrentActivity(Context context) {
        BaseFragment currentFragment;
        int containerId = getContainerIdForCurrentActivity(context);

        currentFragment = (BaseFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(containerId);

        return currentFragment;
    }

    public static int getContainerIdForCurrentActivity(Context context) {
        int containerId = BaseActivity.MAIN_ACTIVITY_CONTAINER_ID;

        if (context instanceof MainActivity) {
            containerId = BaseActivity.MAIN_ACTIVITY_CONTAINER_ID;
        }

        return containerId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.networkManager = NetworkManager.newInstance(getActivity(), TWITTER_API_BASE_URL);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();

        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        return;
    }

    @Override
    public void onResume() {
        super.onResume();

        hideKeyboard(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        //getNetworkManager().cancel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideLoadingIndicator();

        getActivity().invalidateOptionsMenu();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void hideLoadingIndicator() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animateFadeHide(getActivity().findViewById(R.id.pageLoadingIndicator));
                }
            });
        }
    }

    public void showLoadingIndicator() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animateFadeShow(getActivity().findViewById(R.id.pageLoadingIndicator));
                }
            });
        }
    }

    protected void hideToolbar() {
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    protected String getTextFromView(int id) {
        return ((TextView) getView().findViewById(id)).getText().toString();
    }

    protected void addDataForValidString(String value, int viewId) {
        Log.i(BaseFragment.LOG_TAG, value);
        if (!isValidString(value)) {
            value = "-";
        }
        ((TextView) getView().findViewById(viewId)).setText(value);

    }

    public void addImageToImageView(final Activity activity, final String imageUrl, int imageViewId) {
        final ImageView imageView = (ImageView) getView().findViewById(imageViewId);
        //imageView.setImageBitmap(null);

        if (isValidString(imageUrl)) {
            new AsyncTask<String, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    return getBitmapFromUrl(imageUrl);
                }

                @Override
                protected void onPostExecute(final Bitmap imageBitmap) {
                    if (imageBitmap != null) {
                        addBitmapToView(activity, imageBitmap, imageView);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                }
            }.execute();
        }
    }

    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addBitmapToView(Activity activity, final Bitmap imageBitmap, final ImageView imageView) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animateImageAddition(imageBitmap, imageView);
            }
        });
    }

    protected PagerSlidingTabStrip intializePagerTabs(PagerSlidingTabStrip slidingTabs, ViewPager viewPager) {
        slidingTabs.setShouldExpand(true);
        slidingTabs.setTextColorResource(android.R.color.white);
        slidingTabs.setIndicatorColorResource(R.color.menuCta);
        slidingTabs.setIndicatorHeight(slidingTabs.getIndicatorHeight());
        slidingTabs.setUnderlineHeight(0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            slidingTabs.setUnderlineHeight(0);
        }

        slidingTabs.setViewPager(viewPager);
        return slidingTabs;
    }

    protected PagerSlidingTabStrip intializeSubPagerTabs(PagerSlidingTabStrip slidingTabs, ViewPager viewPager) {
        slidingTabs.setShouldExpand(true);
        slidingTabs.setIndicatorHeight(12);
        slidingTabs.setTextColorResource(android.R.color.white);
        slidingTabs.setIndicatorColorResource(R.color.menuCta);
        slidingTabs.setIndicatorHeight(slidingTabs.getIndicatorHeight());
        slidingTabs.setUnderlineHeight(0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            slidingTabs.setUnderlineHeight(0);
        }

        slidingTabs.setViewPager(viewPager);

        return slidingTabs;
    }

    protected void cancelAsyncTask(AsyncTask asyncTask) {
        if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            String asyncTaskLog = asyncTask.getClass().getSimpleName();

            asyncTaskLog += " is being cancelled";
            Log.e(BaseFragment.LOG_TAG, asyncTaskLog);
            asyncTask.cancel(true);
        }
    }

    public String getTitle() {
        return title;
    }

    /**
     * *******************************
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
