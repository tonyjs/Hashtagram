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
import com.tonyjs.hashtagram.io.model.insta.*;
import com.tonyjs.hashtagram.io.request.ResponseListener;
import com.tonyjs.hashtagram.ui.adapter.CommentAdapter;
import com.tonyjs.hashtagram.ui.widget.*;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.RequestFactory;
import com.tonyjs.hashtagram.util.TimeUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by orcpark on 14. 11. 12..
 */
public class DetailFragment extends BaseFragment implements PullCatchListView.OnPullListener {
    public static final String KEY_ITEM = "item";
    public static DetailFragment newInstance(InstaItem item) {
        DetailFragment fragment = new DetailFragment();
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_favorite) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @InjectView(R.id.iv_thumb) ImageView mIvThumb;
    @InjectView(R.id.tv_summary) TextView mTvSummary;
    @InjectView(R.id.iv_author) ImageView mIvAuthor;
    @InjectView(R.id.tv_author) TextView mTvAuthor;
    @InjectView(R.id.tv_created_time) TextView mTvCreatedTime;
    @InjectView(R.id.tv_comment_count) TextView mTvCommentCount;
    @InjectView(R.id.scroll_view) SlipScrollView mScrollView;
    @InjectView(R.id.slip_layout) SlipLayout mSlipLayout;
    @InjectView(R.id.drag_layout) DragLayout mDragLayout;
    @InjectView(R.id.v_metaphor) View mMetaphor;
    @InjectView(R.id.list_view) PullCatchListView mPullCatchListView;
    private CommentAdapter mCommentAdapter;
    private void initLayout() {
        mDragLayout.setMetaphor(mMetaphor);
        mPullCatchListView.setOnPullListener(this);
        mCommentAdapter = new CommentAdapter(mActivity);
        mPullCatchListView.setAdapter(mCommentAdapter);

        InstaItem item = getItem();
        if (item == null) {
            return;
        }
        mTvCreatedTime.setText(TimeUtils.getRelativeTime(item.getCreateTime()));

        InstaUser user = item.getUser();
        if (user != null) {
            String authorUrl = user.getProfilePictureUrl();
            ImageLoader.loadCircleDrawable(mActivity, authorUrl, mIvAuthor);
            mTvAuthor.setText(user.getName());
        }

        InstaComment comment = item.getComments();
        String commentCount = comment != null ? Integer.toString(comment.getCount()) : Integer.toString(0);
        mTvCommentCount.setText(commentCount);

        setLikeViews(item);

        ArrayList<InstaCommentItem> comments = comment.getItems();
        mCommentAdapter.setItems(comments);

        InstaImageInfo info = item.getImageInfo();
        InstaImageSpec spec = info != null ? info.getStandard() : null;
        final String thumbUrl = spec != null ? spec.getUrl() : null;
        ImageLoader.load(mActivity, thumbUrl, mIvThumb, true);
        String summary = item.getCaption() != null ? item.getCaption().getTitle() : null;
        mTvSummary.setText(summary);

        mScrollView.post(mSetSlipLayoutRunnable);
    }

    private InstaItem getItem() {
        Bundle args = getArguments();
        return (InstaItem) args.getSerializable(KEY_ITEM);
    }

    private ResponseListener<JSONObject> mFeedbackCallback =
            new ResponseListener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("jsp", "success - " + response.toString());
                }

                @Override
                public void onError(VolleyError error) {
                    Log.e("jsp", "error - " + error.getMessage());
                }
            };

    private void setLikeViews(final InstaItem item) {
        final boolean userHasLiked = item.userHasLiked();
        mMenuFavorite.setIcon(userHasLiked ?
                R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_outline_white_24dp);

        final InstaLikes likes = item.getLikes();
        String likesCount = likes != null ?
                Integer.toString(likes.getCount()) : Integer.toString(0);

        mMenuFavoriteCount.setTitle(likesCount);

        mMenuFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                handleFeedback(item, userHasLiked);
                item.setUserHasLiked(!userHasLiked);
                likes.setCount(likes.getCount() + (userHasLiked ? -1 : +1));
                setLikeViews(item);
                return true;
            }
        });
    }

    private Toast mToast;
    private void handleFeedback(InstaItem item, boolean userHasLiked) {
        String messageFormat = null;
        if (userHasLiked) {
            messageFormat  = "%s 님의 게시물의 좋아요를 취소합니다.";
            RequestFactory.postUnLike(getActivity(), item.getId(), null, mFeedbackCallback);
        } else {
            messageFormat  = "%s 님의 게시물을 좋아합니다.";
            RequestFactory.postLike(getActivity(), item.getId(), null, mFeedbackCallback);
        }

        if (mToast == null) {
            mToast = Toast.makeText(
                    getActivity(), String.format(messageFormat, item.getUser().getName()), Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(
                    getActivity(), String.format(messageFormat, item.getUser().getName()), Toast.LENGTH_SHORT);
        }

        mToast.show();
    }

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
//                    mScrollView.getChildAt(0).getHeight()
//                            + mScrollView.getPaddingTop() + mScrollView.getPaddingBottom();
            if (contentsHeight > limitHeight) {
                mSlipLayout.setScrollView(mScrollView);
                if (mListener != null) {
                    mListener.onSlipLayoutCreated(mSlipLayout);
                }
            }
        }
    };
}

//    @InjectView(R.id.et_comment) EditText mEtComment;
//    @OnClick(R.id.btn_comment) void postComment() {
//        CharSequence comment = mEtComment.getText();
//        if (!TextUtils.isEmpty(comment)) {
//            InstaItem item = getItem();
//            String id = item != null ? item.getId() : null;
//            if (TextUtils.isEmpty(id)) {
//                return;
//            }
//            RequestFactory.postComment(
//                    mActivity, id, comment.toString(),
//                    null, new ResponseListener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            if (response == null) {
//                                return;
//                            }
//                            Toast.makeText(mActivity, response.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onError(VolleyError error) {
//                            Toast.makeText(mActivity, error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }