package com.orcpark.hashtagram.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.VolleyError;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.config.HashtagConfig;
import com.orcpark.hashtagram.io.JsonParser;
import com.orcpark.hashtagram.io.model.insta.InstaItem;
import com.orcpark.hashtagram.io.model.insta.Instagram;
import com.orcpark.hashtagram.io.request.ResponseListener;
import com.orcpark.hashtagram.ui.adapter.InstaRecyclerAdapter;
import com.orcpark.hashtagram.ui.widget.SlipLayout;
import com.orcpark.hashtagram.util.RequestFactory;
import com.orcpark.hashtagram.util.UiUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class InstagramRecyclerFragment extends BaseFragment
        implements ResponseListener<JSONObject>, SwipeRefreshLayout.OnRefreshListener,
                    InstaRecyclerAdapter.RequestMoreListener{

    public interface Listener {
        public void onAttach(InstagramRecyclerFragment fragment);

        public void onDetach(InstagramRecyclerFragment fragment);

        public void onActivityCreated(InstagramRecyclerFragment fragment);
    }

    public static final String KEY_HASH_TAG = "hash_tag";
    public static final String KEY_POSITION = "position";

    public static InstagramRecyclerFragment newInstance(int position, String tag) {
        InstagramRecyclerFragment fragment = new InstagramRecyclerFragment();
        Bundle args = new Bundle();
        args.putString(KEY_HASH_TAG, tag);
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String DEFAULT_HASH_TAG = "Hello";

    private String mHashTag;
    private int mPosition;
    private boolean mIsHashtag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mHashTag = args != null ? args.getString(KEY_HASH_TAG) : DEFAULT_HASH_TAG;
        mPosition = args != null ? args.getInt(KEY_POSITION) : 0;
        mIsHashtag = !HashtagConfig.NEWSFEED.equals(mHashTag);
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            ((Listener) activity).onAttach(this);
        }
    }

    private View mProgressBar;

    private SlipLayout mSlipLayout;

    private SwipeRefreshLayout mSwipeLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private InstaRecyclerAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_instagram_recycler, container, false);

        mProgressBar = rootView.findViewById(R.id.progress_bar);

        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        mSwipeLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        int paddingTop = UiUtils.getDPFromPixelSize(mActivity, 56);
        mSwipeLayout.setProgressViewOffset(false, paddingTop, paddingTop * 2);
        mSwipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mListAdapter = new InstaRecyclerAdapter(mActivity.getBaseContext());
        mListAdapter.setRequestMoreListener(this);

        mRecyclerView.setAdapter(mListAdapter);

        mSlipLayout = (SlipLayout) rootView.findViewById(R.id.layout_translate);
        mSlipLayout.setRecyclerView(mRecyclerView);

        return rootView;
    }

    public SlipLayout getSlipLayout() {
        return mSlipLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mActivity instanceof Listener) {
            ((Listener) mActivity).onActivityCreated(this);
        }

        onRefresh();
    }

    @Override
    public void onRefresh() {
        getInstagram(null);
    }

    @Override
    public void onRequestMore() {
        getInstagram(mNextUrl);
    }

    private boolean mInAsync = false;
    private void getInstagram(String nextUrl) {
        if (mInAsync) {
            mSwipeLayout.setRefreshing(false);
            return;
        }

        mInAsync = true;

        if (!TextUtils.isEmpty(nextUrl)) {
//            Toast.makeText(mActivity, nextUrl, Toast.LENGTH_LONG).show();
            RequestFactory.getNextItems(getActivity(), nextUrl, null, mMoreResponseListener);
            return;
        }

        if (!mIsHashtag) {
            RequestFactory.getNewsfeed(getActivity(), mProgressBar, this);
//            RequestUtils.getNewsfeed(getActivity(), mProgressBar, this);
        } else {
            RequestFactory.getHashTag(getActivity(), mHashTag, mProgressBar, this);
//            RequestUtils.getHashTags(getActivity(), mHashTag, mProgressBar, this);
        }
    }

    String mNextUrl;
    @Override
    public void onResponse(JSONObject jsonObject) {
        if(validateAndHandleRefresh(jsonObject)){
            Instagram instagram = JsonParser.getInstagram(jsonObject);
            ArrayList<InstaItem> items = instagram.getInstaItems();
            mNextUrl = items != null && items.size() >= 0 ? instagram.getNextUrl() : null;
            mListAdapter.setItems(items);
        }
    }

    @Override
    public void onError(VolleyError error) {
        mSwipeLayout.setRefreshing(false);
    }

    private boolean validateAndHandleRefresh(JSONObject jsonObject) {
        if (mActivity == null || mActivity.isFinishing()) {
            return false;
        }

        mInAsync = false;
        mSwipeLayout.setRefreshing(false);

        if (jsonObject == null) {
            return false;
        }

        return true;
    }

    ResponseListener<JSONObject> mMoreResponseListener =
            new ResponseListener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if(validateAndHandleRefresh(jsonObject)){
                        Instagram instagram = JsonParser.getInstagram(jsonObject);
                        ArrayList<InstaItem> items = instagram.getInstaItems();
                        mNextUrl = items != null && items.size() >= 0 ? instagram.getNextUrl() : null;
                        mListAdapter.addItems(items);
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    mSwipeLayout.setRefreshing(false);
                }
            };

    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity instanceof Listener) {
            ((Listener) mActivity).onDetach(this);
        }
    }
}
