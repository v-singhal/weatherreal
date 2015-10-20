package com.vbstudio.weather.dom;

import com.vbstudio.weather.fragment.BaseFragment;

/**
 * Created by vaibhav on 23/8/15.
 */
public class EmbeddedFragmentData {

    private BaseFragment baseFragment;
    private String title;

    public BaseFragment getBaseFragment() {
        return baseFragment;
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
