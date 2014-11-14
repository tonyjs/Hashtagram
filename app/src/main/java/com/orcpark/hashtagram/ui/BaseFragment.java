package com.orcpark.hashtagram.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by tony.park on 2014. 9. 26..
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public boolean isFinishing() {
        if (mActivity == null || mActivity.isFinishing()) {
            return true;
        }
        return false;
    }
}
