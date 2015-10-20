package com.vbstudio.weather.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static com.vbstudio.weather.utils.UIUtils.getDisplayMetrics;

public class BaseDialog extends Dialog implements View.OnClickListener {

    protected int computedWidth = 0;
    private Context context;

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void show() {
        super.show();

        int screenWidth = getDisplayMetrics(context).widthPixels;
        int widthMargin = 60;
        computedWidth = screenWidth - widthMargin;
        getWindow().setLayout(computedWidth, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    protected void setupView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onClick(View v) {

    }
}
