package com.tonyjs.hashtagram.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.tonyjs.hashtagram.ui.widget.SlipLayout;

/**
 * Created by tony.park on 2014. 9. 26..
 */
public abstract class BaseFragment extends Fragment {
    public interface OnSlipLayoutCreatedListener {
        public void onSlipLayoutCreated(SlipLayout slipLayout);
    }

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (mActivity instanceof OnSlipLayoutCreatedListener) {
            mListener = (OnSlipLayoutCreatedListener) mActivity;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected OnSlipLayoutCreatedListener mListener;

    @Override
    public void onDetach() {
        mListener = null;
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
