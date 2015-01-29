package com.tonyjs.hashtagram.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.volley.VolleyError;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.config.HashtagConfig;
import com.tonyjs.hashtagram.io.model.Feed;
import com.tonyjs.hashtagram.io.model.Pagination;
import com.tonyjs.hashtagram.io.request.volley.RequestProvider;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;
import com.tonyjs.hashtagram.io.response.NewsFeedResponse;
import com.tonyjs.hashtagram.ui.adapter.TimeLineListAdapter;
import com.tonyjs.hashtagram.ui.adapter.TimeLineListAdapterWithVolley;
import com.tonyjs.hashtagram.util.UiUtils;

import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class FeedListFragment extends BaseFragment
        implements Callback<NewsFeedResponse>,
                    SwipeRefreshLayout.OnRefreshListener,
                    TimeLineListAdapter.NeedMoreCallback,
                    ListView.OnItemClickListener{

    public static final int REQUEST_CODE_MANAGE_ITEM = 1;

    public interface LifecycleCallback {
        public void onViewCreated(ListView listView);

        public void onResume(FeedListFragment fragment);
    }

    public static final String KEY_HASH_TAG = "hash_tag";
    public static final String KEY_POSITION = "position";

    public static FeedListFragment newInstance(int position, String tag) {
        FeedListFragment fragment = new FeedListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_HASH_TAG, tag);
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String DEFAULT_HASH_TAG = "Hello";

    private String mHashTag;
    private int mPosition;
    private boolean mHashtag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mHashTag = args != null ? args.getString(KEY_HASH_TAG) : DEFAULT_HASH_TAG;
        mPosition = args != null ? args.getInt(KEY_POSITION) : 0;
        mHashtag = !HashtagConfig.NEWSFEED.equals(mHashTag);
    }

    public String getHashTag() {
        return mHashTag;
    }

    public int getPosition() {
        return mPosition;
    }

    @InjectView(R.id.progress_bar) View mProgressBar;
    @InjectView(R.id.swipe_layout) SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.list_view) ListView mListView;
    private TimeLineListAdapter mAdapter;
    private View mFooterView;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed_list, container, false);
        ButterKnife.inject(this, rootView);
        mSwipeLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        int paddingTop = UiUtils.getDPFromPixelSize(mActivity, 56);
        mSwipeLayout.setProgressViewOffset(false, paddingTop, paddingTop * 2);
        mSwipeLayout.setOnRefreshListener(this);

        mFooterView = inflater.inflate(R.layout.layout_footer, mListView, false);
        mFooterView.setVisibility(View.GONE);
        mListView.addFooterView(mFooterView, null, false);
        mAdapter = new TimeLineListAdapter(mActivity);
//        mAdapter = new TimeLineListAdapterWithVolley(mActivity.getBaseContext(), getImageLoader());
        mListView.setOnItemClickListener(this);
        mAdapter.setNeedMoreCallback(this);
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFinishing()) {
            return;
        }
        if (mActivity instanceof LifecycleCallback) {
            ((LifecycleCallback) mActivity).onResume(this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        if (mActivity instanceof LifecycleCallback) {
            ((LifecycleCallback) mActivity).onViewCreated(mListView);
        }

        onRefresh();
    }

    @Override
    public void onRefresh() {
        getNewsFeed(null);
    }

    @Override
    public void onNeedMore() {
        getNewsFeed(mNextUrl);
    }

    private boolean mInAsync = false;
    private void getNewsFeed(String nextUrl) {
        if (isFinishing()) {
            return;
        }

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
            RequestProvider.getNewsFeed(getActivity(), nextUrl, null, mNeedMoreFeedCallback);
            return;
        }

        if (!mHashtag) {
            RequestProvider.getNewsFeed(getActivity(), mProgressBar, this);
        } else {
            RequestProvider.getHashTaggedFeed(getActivity(), mHashTag, mProgressBar, this);
        }
    }

    private String mNextUrl;
    @Override
    public void onSuccess(NewsFeedResponse response) {
        if(validateAndHandleRefresh(response)){
            ArrayList<Feed> items = response.getData();
            Pagination pagination = response.getPagination();
            mNextUrl = pagination != null ? pagination.getNextUrl() : null;
            mCanRequestMore = !TextUtils.isEmpty(mNextUrl);
            mAdapter.setItems(items);
            if (!mCanRequestMore) {
                mListView.removeFooterView(mFooterView);
            } else {
                mFooterView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        mSwipeLayout.setRefreshing(false);
        mCanRequestMore = false;
        mInAsync = false;
        mListView.removeFooterView(mFooterView);
    }

    private boolean validateAndHandleRefresh(NewsFeedResponse response) {
        if (isFinishing()) {
            return false;
        }

        mInAsync = false;
        mSwipeLayout.setRefreshing(false);

        return response != null;
    }

    private boolean mCanRequestMore = true;
    private Callback<NewsFeedResponse> mNeedMoreFeedCallback =
            new Callback<NewsFeedResponse>() {
                @Override
                public void onSuccess(NewsFeedResponse response) {
                    handleMoreFeedResponse(response);
                }

                @Override
                public void onError(VolleyError error) {
                    mSwipeLayout.setRefreshing(false);
                    mCanRequestMore = false;
                    mInAsync = false;
                    mListView.removeFooterView(mFooterView);
                }
            };

    private void handleMoreFeedResponse(NewsFeedResponse response) {
        if (!validateAndHandleRefresh(response)) {
            return;
        }
        ArrayList<Feed> items = response.getData();
        Pagination pagination = response.getPagination();
        mNextUrl = pagination != null ?
                pagination.getNextUrl() : null;
        mAdapter.addItems(items);
        mCanRequestMore = !TextUtils.isEmpty(mNextUrl);
        if (!mCanRequestMore) {
            mListView.removeFooterView(mFooterView);
        } else {
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Feed item = mAdapter.getItem(position);
        Intent intent = new Intent(mActivity, FeedDetailActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("hashtag", mHashTag);
        startActivityForResult(intent, REQUEST_CODE_MANAGE_ITEM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_ITEM
                && resultCode == Activity.RESULT_OK
                && data != null) {
            Feed item = (Feed) data.getSerializableExtra("item");
            handleFeedChanged(item);
        }
    }

    private void handleFeedChanged(Feed item) {
        if (isFinishing()) {
            return;
        }
        int max = mAdapter.getItems().size();
        for (int i = 0; i < max; i++) {
            Feed t = mAdapter.getItem(i);
            if (t.getId().equals(item.getId())) {
                t.setUserLiked(item.isUserLiked());
                t.setLikes(item.getLikes());
                t.setComments(item.getComments());
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

}
