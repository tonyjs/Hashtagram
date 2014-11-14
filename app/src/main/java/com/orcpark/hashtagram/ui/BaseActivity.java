package com.orcpark.hashtagram.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by orcpark on 14. 11. 13..
 */
public abstract class BaseActivity extends ActionBarActivity {

    public abstract int getContainerResId();

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
                        ((Object)fragment).getClass().getSimpleName())
                .addToBackStack(stackName)
                .commitAllowingStateLoss();
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

    protected Toolbar mToolBar;
    public Toolbar getToolBar() {
        return mToolBar;
    }
}
