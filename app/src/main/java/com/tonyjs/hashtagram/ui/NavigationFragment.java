package com.tonyjs.hashtagram.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.db.HashtagramDatabase;
import com.tonyjs.hashtagram.io.model.NavigationItem;
import com.tonyjs.hashtagram.io.model.User;
import com.tonyjs.hashtagram.ui.adapter.base.BasicRecyclerAdapter;
import com.tonyjs.hashtagram.ui.adapter.base.BasicViewHolder;
import com.tonyjs.hashtagram.ui.widget.BasicRecyclerView;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.PrefUtils;

import java.util.ArrayList;

/**
 * Created by tony.park on 15. 1. 3..
 */
public class NavigationFragment extends Fragment
                implements BasicRecyclerView.OnItemClickListener{

    public interface NavigationCallback {
        public void onItemSelected(NavigationItem item);
    }

    public interface LifecycleCallback {
        public void onFragmentCreated(NavigationFragment fragment);
    }

    @InjectView(R.id.recycler_view) BasicRecyclerView mRecyclerView;
    @InjectView(R.id.tv_name) TextView mTvName;
    @InjectView(R.id.tv_profile) TextView mTvProfile;
    @InjectView(R.id.iv_user) ImageView mIvUser;
    private LinearLayoutManager mLayoutManager;
    private NavigationAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.inject(this, rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter = new NavigationAdapter(getActivity()));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof LifecycleCallback) {
            ((LifecycleCallback) getActivity()).onFragmentCreated(this);
        }

        updateUi();
    }

    public void updateUi() {
        User user = PrefUtils.getUser(getActivity());
        if (user == null) {
            return;
        }

        mTvName.setText(user.getName());
        String profile = user.getBio();
        if (TextUtils.isEmpty(profile)) {
            profile = user.getFullName();
        }
        mTvProfile.setText(profile);
        ImageLoader.loadCircleDrawable(getActivity(), user.getProfileImageUrl(), mIvUser);

        updateNavigationItems();

        checkNavigation(0);
    }

    public void updateNavigationItems() {
        ArrayList<String> items = HashtagramDatabase.getInstance(getActivity()).getAllHashTag();

        if (items == null || items.size() <= 0) {
            return;
        }

        ArrayList<NavigationItem> pageItems = new ArrayList<>();
        int i = 0;
        for (String hashTag : items) {
            pageItems.add(new NavigationItem(i, hashTag));
            i++;
        }

        mAdapter.setItems(pageItems);
    }

    public void checkLatestNavigation() {
        checkNavigation(mAdapter.getItemCount() - 1);
    }

    public void checkNavigation(int position) {
        if (position >= mAdapter.getItemCount()) {
            return;
        }
        callback(position);
    }

    private void revertItems() {
        int max = mAdapter.getItemCount();
        for (int i = 0; i < max; i++) {
            NavigationItem item = mAdapter.getItem(i);
            item.setSelected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    private NavigationCallback mCallback;

    public void setNavigationCallback(NavigationCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onItemClick(BasicRecyclerView parent, View child, int position, long id) {
        callback(position);
    }

    private void callback(int position) {
        if (mCallback == null) {
            return;
        }

        revertItems();

        NavigationItem item = mAdapter.getItem(position);
        item.setSelected(true);
        mAdapter.notifyItemChanged(position);

        mCallback.onItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    class NavigationAdapter extends BasicRecyclerAdapter<NavigationItem> {

        public NavigationAdapter(Context context) {
            super(context);
        }

        @Override
        public BasicViewHolder getViewHolder(ViewGroup parent, int viewType) {
            View itemView =
                    getLayoutInflater().inflate(R.layout.recycler_item_navigation, parent, false);
            return new NavigationHolder(getContext(), itemView);
        }

        @Override
        public void onBindViewHolder(BasicViewHolder viewHolder, int position) {
            viewHolder.onBindView(getItem(position));
        }

        class NavigationHolder extends BasicViewHolder<NavigationItem> {

            private TextView mTvTitle;

            public NavigationHolder(Context context, View itemView) {
                super(context, itemView);
                mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            }

            @Override
            public void onBindView(NavigationItem item) {
                mTvTitle.setText(item.getHashTag());
                itemView.setSelected(item.isSelected());
            }
        }
    }

}
