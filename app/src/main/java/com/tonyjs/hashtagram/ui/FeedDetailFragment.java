package com.tonyjs.hashtagram.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.volley.VolleyError;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.model.Comment;
import com.tonyjs.hashtagram.io.model.Comments;
import com.tonyjs.hashtagram.io.model.Feed;
import com.tonyjs.hashtagram.io.model.ImageResolution;
import com.tonyjs.hashtagram.io.model.Images;
import com.tonyjs.hashtagram.io.model.Likes;
import com.tonyjs.hashtagram.io.model.User;
import com.tonyjs.hashtagram.io.request.volley.RequestProvider;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;
import com.tonyjs.hashtagram.ui.adapter.CommentAdapter;
import com.tonyjs.hashtagram.ui.widget.*;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.TimeUtils;
import com.tonyjs.hashtagram.util.ToastManager;

import java.util.ArrayList;

/**
 * Created by tonyjs on 14. 11. 12..
 */
public class FeedDetailFragment extends BaseFragment implements PullCatchListView.OnPullListener {
    public interface SlipControlCallback {
        public void onControllerCreated(SlipScrollView slipScrollView);
    }

    public static final String KEY_ITEM = "item";
    public static FeedDetailFragment newInstance(Feed item) {
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    private MenuItem mMenuFavorite;
    private MenuItem mMenuFavoriteCount;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);

        mMenuFavorite = menu.findItem(R.id.action_favorite);
        mMenuFavoriteCount = menu.findItem(R.id.favorite_count);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        initLayout();
    }

    @InjectView(R.id.iv_thumb) ImageView mIvThumb;
    @InjectView(R.id.tv_summary) TextView mTvSummary;
    @InjectView(R.id.iv_author) ImageView mIvAuthor;
    @InjectView(R.id.tv_author) TextView mTvAuthor;
    @InjectView(R.id.tv_created_time) TextView mTvCreatedTime;
    @InjectView(R.id.tv_comment_count) TextView mTvCommentCount;
    @InjectView(R.id.scroll_view) SlipScrollView mScrollView;
    @InjectView(R.id.drag_layout) DragLayout mDragLayout;
    @InjectView(R.id.v_metaphor) View mMetaphor;
    @InjectView(R.id.list_view) PullCatchListView mPullCatchListView;
    private CommentAdapter mCommentAdapter;
    private void initLayout() {
        mDragLayout.setMetaphor(mMetaphor);
        mPullCatchListView.setOnPullListener(this);
        mCommentAdapter = new CommentAdapter(mActivity);
        mPullCatchListView.setAdapter(mCommentAdapter);

        Feed item = getItem();
        if (item == null) {
            return;
        }
        long createdTime = Long.valueOf(item.getCreatedTime());
        mTvCreatedTime.setText(TimeUtils.getRelativeTime(createdTime));

        User user = item.getUser();
        if (user != null) {
            String authorUrl = user.getProfileImageUrl();
            ImageLoader.loadCircleDrawable(mActivity, authorUrl, mIvAuthor);
            mTvAuthor.setText(user.getName());
        }

        Comments comment = item.getComments();
        String commentCount = comment != null ? Integer.toString(comment.getCount()) : Integer.toString(0);
        mTvCommentCount.setText(commentCount);

        setLikeViews(item);

        ArrayList<Comment> comments = comment.getData();
        mCommentAdapter.setItems(comments);

        Images info = item.getImages();
        ImageResolution spec = info != null ? info.getStandard() : null;
        final String thumbUrl = spec != null ? spec.getUrl() : null;
        ImageLoader.load(mActivity, thumbUrl, mIvThumb, true);
        String summary = item.getCaption() != null ? item.getCaption().getTitle() : null;
        mTvSummary.setText(summary);

        mScrollView.post(mSetSlipLayoutRunnable);
    }

    private Feed getItem() {
        Bundle args = getArguments();
        return (Feed) args.getSerializable(KEY_ITEM);
    }

    private void setLikeViews(final Feed item) {
        final boolean userHasLiked = item.isUserLiked();
        mMenuFavorite.setIcon(userHasLiked ?
                R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_outline_white_24dp);

        final Likes likes = item.getLikes();
        String likesCount = likes != null ?
                Integer.toString(likes.getCount()) : Integer.toString(0);

        mMenuFavoriteCount.setTitle(likesCount);

        mMenuFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                handleFeedback(item, userHasLiked);
                item.setUserLiked(!userHasLiked);
                likes.setCount(likes.getCount() + (userHasLiked ? -1 : +1));
                setLikeViews(item);
                return true;
            }
        });
    }

    private Toast mToast;
    private void handleFeedback(Feed item, boolean userHasLiked) {
        String messageFormat = null;
        if (userHasLiked) {
            messageFormat  = "%s 님의 게시물의 좋아요를 취소합니다.";
            RequestProvider.postUnLikes(mActivity, item.getId(), null, mFeedbackCallback);
        } else {
            messageFormat  = "%s 님의 게시물을 좋아합니다.";
            RequestProvider.postLikes(mActivity, item.getId(), null, mFeedbackCallback);
        }

        if (isFinishing()) {
            return;
        }
        ToastManager.getInstance().show(
                mActivity, String.format(messageFormat, item.getUser().getName()));
    }

    private Callback<String> mFeedbackCallback =
            new Callback<String>() {
                @Override
                public void onSuccess(String response) {
                    Log.e("jsp", "success - " + response.toString());
                }

                @Override
                public void onError(VolleyError e) {
                    Log.e("jsp", "error - " + e.getMessage());
                }
            };


    @Override
    public void onPulled() {
        mDragLayout.collapsePane();
    }

    private Runnable mSetSlipLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            int limitHeight = mScrollView.getHeight();
            int contentsHeight =
                    mScrollView.getChildAt(0).getHeight();
            if (contentsHeight > limitHeight) {
                if (mActivity != null &&
                        mActivity instanceof SlipControlCallback) {
                    ((SlipControlCallback) mActivity).onControllerCreated(mScrollView);
                }
            }
        }
    };
}