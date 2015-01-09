package com.tonyjs.hashtagram.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setClickable(true);
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
