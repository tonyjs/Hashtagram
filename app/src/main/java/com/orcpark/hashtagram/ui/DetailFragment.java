package com.orcpark.hashtagram.ui;

import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.volley.VolleyError;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.*;
import com.orcpark.hashtagram.io.request.ResponseListener;
import com.orcpark.hashtagram.ui.adapter.CommentAdapter;
import com.orcpark.hashtagram.ui.widget.DragLayout;
import com.orcpark.hashtagram.ui.widget.PullCatchListView;
import com.orcpark.hashtagram.ui.widget.SlipLayout;
import com.orcpark.hashtagram.ui.widget.SlipScrollView;
import com.orcpark.hashtagram.util.ImageLoader;
import com.orcpark.hashtagram.util.RequestFactory;
import com.orcpark.hashtagram.util.TimeUtils;
import com.orcpark.hashtagram.util.UiUtils;
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
        initLayout();
        return rootView;
    }

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
            ImageLoader.load(mActivity, authorUrl, mIvAuthor, true);
            mTvAuthor.setText(user.getName());
        }

        InstaComment comment = item.getComments();
        String commentCount = comment != null ? Integer.toString(comment.getCount()) : Integer.toString(0);
        mTvCommentCount.setText(commentCount);

        ArrayList<InstaCommentItem> comments = comment.getItems();
        mCommentAdapter.setItems(comments);

        mScrollView.post(mSetSlipLayoutRunnable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InstaItem item = getItem();
        if (item == null) {
            return;
        }

        InstaImageInfo info = item.getImageInfo();
        InstaImageSpec spec = info != null ? info.getStandard() : null;
        final String thumbUrl = spec != null ? spec.getUrl() : null;
        ImageLoader.load(mActivity, thumbUrl, mIvThumb);

        String summary = item.getCaption() != null ? item.getCaption().getTitle() : null;
        mTvSummary.setText(summary);
    }

    private InstaItem getItem() {
        Bundle args = getArguments();
        return (InstaItem) args.getSerializable(KEY_ITEM);
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