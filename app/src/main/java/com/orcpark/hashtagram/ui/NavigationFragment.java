package com.orcpark.hashtagram.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.InstaUser;
import com.orcpark.hashtagram.ui.widget.BasicRecyclerView;
import com.orcpark.hashtagram.util.ImageLoader;
import com.orcpark.hashtagram.util.PrefUtils;

/**
 * Created by tony.park on 15. 1. 3..
 */
public class NavigationFragment extends Fragment {
    public interface Callback {
        public void onFragmentCreated(NavigationFragment fragment);
    }

    @InjectView(R.id.recycler_view) BasicRecyclerView mRecyclerView;
    @InjectView(R.id.tv_name) TextView mTvName;
    @InjectView(R.id.tv_profile) TextView mTvProfile;
    @InjectView(R.id.iv_user) ImageView mIvUser;
    private LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.inject(this, rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof Callback) {
            ((Callback) getActivity()).onFragmentCreated(this);
        }

        updateUi();
    }

    public void updateUi() {
        InstaUser user = PrefUtils.getUser(getActivity());
        if (user == null) {
            return;
        }

        mTvName.setText(user.getName());
        mTvProfile.setText(user.getBio());
        ImageLoader.loadCircleDrawable(getActivity(), user.getProfilePictureUrl(), mIvUser);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
