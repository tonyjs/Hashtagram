package com.tonyjs.hashtagram.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.ui.widget.SlipLayoutController;

/**
 * Created by orcpark on 14. 11. 13..
 */
public abstract class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSlipController = new SlipLayoutController();
    }

    protected Toolbar mToolBar;
    protected Toolbar getToolBar() {
        if (mToolBar == null) {
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            mToolBar.setTitleTextColor(Color.WHITE);
        }
        return mToolBar;
    }

    private SlipLayoutController mSlipController;
    public SlipLayoutController getSlipController(){
        return mSlipController;
    }

    public abstract int getContainerResId();

    public void addFragment(int containerResId, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerResId, fragment,
                        ((Object)fragment).getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    public void addFragment(int containerResId, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerResId, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContainerResId(), fragment,
                        ((Object)fragment).getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, String stackName) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContainerResId(), fragment,
                        ((Object) fragment).getClass().getSimpleName())
                .addToBackStack(stackName)
                .commitAllowingStateLoss();
    }

    public void addFragment(int containerResId, Fragment fragment, String tag, String stackName,
                            int enterAnim, int exitEnim, int popEnterAnim, int popExitAnim) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(enterAnim, exitEnim, popEnterAnim, popExitAnim)
                .add(containerResId, fragment, tag)
                .addToBackStack(stackName)
                .commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContainerResId(), fragment,
                        ((Object) fragment).getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContainerResId(), fragment, tag)
                .commitAllowingStateLoss();
    }

    public void detachFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null) {
            fm.beginTransaction().detach(fragment).commitAllowingStateLoss();
        }
    }

    public void popAllBackStack() {
        int max = getSupportFragmentManager().getBackStackEntryCount();
        if (max <= 0) {
            return;
        }

        for (int i = 0; i < max; i++) {
            getSupportFragmentManager().popBackStackImmediate();
        }

    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStackImmediate();
    }

}
