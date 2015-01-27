package com.tonyjs.hashtagram.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tonyjs.hashtagram.util.ImageLoader;

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

    private ImageLoader mImageLoader;
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = new ImageLoader(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setClickable(true);
    }

    @Override
    public void onDetach() {
        mImageLoader = null;
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
