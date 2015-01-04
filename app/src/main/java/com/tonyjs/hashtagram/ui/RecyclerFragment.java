package com.tonyjs.hashtagram.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.config.HashtagConfig;
import com.tonyjs.hashtagram.io.JsonParser;
import com.tonyjs.hashtagram.io.model.insta.InstaItem;
import com.tonyjs.hashtagram.io.model.insta.Instagram;
import com.tonyjs.hashtagram.io.request.ResponseListener;
import com.tonyjs.hashtagram.ui.adapter.TimeLineRecyclerAdapter;
import com.tonyjs.hashtagram.ui.widget.BasicRecyclerView;
import com.tonyjs.hashtagram.ui.widget.SlipLayout;
import com.tonyjs.hashtagram.util.RequestFactory;
import com.tonyjs.hashtagram.util.UiUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class RecyclerFragment extends BaseFragment
        implements ResponseListener<JSONObject>,
                    SwipeRefreshLayout.OnRefreshListener,
                    TimeLineRecyclerAdapter.RequestMoreListener,
                    BasicRecyclerView.OnItemClickListener{

    public static final int REQUEST_CODE_MANAGE_ITEM = 1;

    public interface LifecycleCallback {
        public void onFragmentCreated(RecyclerFragment fragment);

        public void onResume(RecyclerFragment fragment);
    }

    public static final String KEY_HASH_TAG = "hash_tag";
    public static final String KEY_POSITION = "position";

    public static RecyclerFragment newInstance(int position, String tag) {
        RecyclerFragment fragment = new RecyclerFragment();
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

    public String getHashTag() {
        return mHashTag;
    }

    public int getPosition() {
        return mPosition;
    }

    private View mProgressBar;

    private SlipLayout mSlipLayout;

    private SwipeRefreshLayout mSwipeLayout;

    private BasicRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TimeLineRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recycler, container, false);

        mProgressBar = rootView.findViewById(R.id.progress_bar);

        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        mSwipeLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        int paddingTop = UiUtils.getDPFromPixelSize(mActivity, 56);
        mSwipeLayout.setProgressViewOffset(false, paddingTop, paddingTop * 2);
        mSwipeLayout.setOnRefreshListener(this);

        mRecyclerView = (BasicRecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TimeLineRecyclerAdapter(mActivity.getBaseContext());
        mRecyclerView.setOnItemClickListener(this);
//        if(mActivity instanceof BasicRecyclerAdapter.OnItemClickListener) {
//            mAdapter.setOnItemClickListener((BasicRecyclerAdapter.OnItemClickListener) mActivity);
//        }
        mAdapter.setRequestMoreListener(this);

        mRecyclerView.setAdapter(mAdapter);

        mSlipLayout = (SlipLayout) rootView.findViewById(R.id.layout_translate);
        mSlipLayout.setRecyclerView(mRecyclerView);

        return rootView;
    }

    public SlipLayout getSlipLayout() {
        return mSlipLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity instanceof LifecycleCallback) {
            ((LifecycleCallback) mActivity).onResume(this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mActivity instanceof LifecycleCallback) {
            ((LifecycleCallback) mActivity).onFragmentCreated(this);
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

        if (!mCanRequestMore) {
            mSwipeLayout.setRefreshing(false);
            return;
        }

        mInAsync = true;

        if (!TextUtils.isEmpty(nextUrl)) {
            RequestFactory.getNextItems(getActivity(), nextUrl, null, mMoreResponseListener);
            return;
        }

        if (!mIsHashtag) {
            RequestFactory.getNewsfeed(getActivity(), mProgressBar, this);
        } else {
            RequestFactory.getHashTag(getActivity(), mHashTag, mProgressBar, this);
        }
    }

    String mNextUrl;
    @Override
    public void onResponse(JSONObject jsonObject) {
        if(validateAndHandleRefresh(jsonObject)){
            Instagram instagram = JsonParser.getInstagram(jsonObject);

            ArrayList<InstaItem> items = instagram.getInstaItems();
            mNextUrl = instagram.getNextUrl();
            mCanRequestMore = !TextUtils.isEmpty(mNextUrl);
            mAdapter.setItems(items);
            if (!mCanRequestMore) {
                mAdapter.removeProgress();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        mSwipeLayout.setRefreshing(false);
        mCanRequestMore = false;
        mInAsync = false;
        mAdapter.removeProgress();
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
    private boolean mCanRequestMore = true;
    ResponseListener<JSONObject> mMoreResponseListener =
            new ResponseListener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if(validateAndHandleRefresh(jsonObject)){
                        Instagram instagram = JsonParser.getInstagram(jsonObject);
                        ArrayList<InstaItem> items = instagram.getInstaItems();
                        mNextUrl = instagram.getNextUrl();
                        mAdapter.addItems(items);
                        mCanRequestMore = !TextUtils.isEmpty(mNextUrl);
                        if (!mCanRequestMore) {
                            mAdapter.removeProgress();
                        }
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    mSwipeLayout.setRefreshing(false);
                    mCanRequestMore = false;
                    mInAsync = false;
                    mAdapter.removeProgress();
                }
            };

    @Override
    public void onItemClick(BasicRecyclerView parent, View child, int position, long id) {
        InstaItem item = mAdapter.getItem(position);
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("hashtag", mHashTag);
        startActivityForResult(intent, REQUEST_CODE_MANAGE_ITEM);
//        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_ITEM
                && resultCode == Activity.RESULT_OK
                && data != null) {
            InstaItem item = (InstaItem) data.getSerializableExtra("item");
            int max = mAdapter.getItems().size();
            for (int i = 0; i < max; i++) {
                InstaItem t = mAdapter.getItem(i);
                if (t.getId().equals(item.getId())) {
                    t.setUserHasLiked(item.userHasLiked());
                    t.setLikes(item.getLikes());
                    t.setComments(item.getComments());
                    mAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }
}
