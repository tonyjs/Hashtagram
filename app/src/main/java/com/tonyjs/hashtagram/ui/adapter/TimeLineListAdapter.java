package com.tonyjs.hashtagram.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.model.Comments;
import com.tonyjs.hashtagram.io.model.Feed;
import com.tonyjs.hashtagram.io.model.ImageResolution;
import com.tonyjs.hashtagram.io.model.Images;
import com.tonyjs.hashtagram.io.model.Likes;
import com.tonyjs.hashtagram.io.model.User;
import com.tonyjs.hashtagram.io.request.volley.RequestProvider;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;
import com.tonyjs.hashtagram.ui.adapter.base.BasicAdapter;
import com.tonyjs.hashtagram.ui.adapter.base.SparseViewHolder;
import com.tonyjs.hashtagram.ui.widget.GradientSquareImageView;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.TimeUtils;
import com.tonyjs.hashtagram.util.ToastManager;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class TimeLineListAdapter extends BasicAdapter<Feed> {
    public interface NeedMoreCallback {
        public void onNeedMore();
    }

    private NeedMoreCallback mNeedMoreCallback;

    public void setNeedMoreCallback(NeedMoreCallback needMoreCallback) {
        mNeedMoreCallback = needMoreCallback;
    }

    private final String LIKES;
    private final String UNLIKES;
    private ImageLoader mImageLoader;
    public TimeLineListAdapter(Context context, ImageLoader imageLoader) {
        super(context);
        mImageLoader = imageLoader;
        LIKES = context.getResources().getString(R.string.likes);
        UNLIKES = context.getResources().getString(R.string.unlikes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_insta, parent, false);
        }

        GradientSquareImageView ivThumb = SparseViewHolder.get(convertView, R.id.iv_thumb);
        TextView tvSummary = SparseViewHolder.get(convertView, R.id.tv_summary);
        ImageView ivAuthor = SparseViewHolder.get(convertView, R.id.iv_author);
        TextView tvAuthor = SparseViewHolder.get(convertView, R.id.tv_author);
        TextView tvCreatedTime = SparseViewHolder.get(convertView, R.id.tv_create_time);
        TextView tvComments = SparseViewHolder.get(convertView, R.id.tv_comment_count);
        TextView tvLikesCount = SparseViewHolder.get(convertView, R.id.tv_likes_count);
        View btnLike = SparseViewHolder.get(convertView, R.id.btn_like);

        Feed item = getItem(position);

        long createdTime = Long.valueOf(item.getCreatedTime());
        tvCreatedTime.setText(TimeUtils.getRelativeTime(createdTime));

        String summary = item.getCaption() != null ? item.getCaption().getText() : "";
        tvSummary.setText(summary);

        Images info = item.getImages();
        ImageResolution spec = info != null ? info.getStandard() : null;
        final String thumbUrl = spec != null ? spec.getUrl() : null;
        User user = item.getUser();
        if (user != null) {
            String authorUrl = user.getProfileImageUrl();

            if (mImageLoader != null && !TextUtils.isEmpty(authorUrl)) {
                mImageLoader.load(ivAuthor, authorUrl, ImageLoader.TransformationType.CIRCLE);
            } else {
                ivThumb.setImageDrawable(null);
            }
            tvAuthor.setText(user.getName());
        }

        Comments comment = item.getComments();
        String commentCount = comment != null ?
                Integer.toString(comment.getCount()) : Integer.toString(0);
        tvComments.setText(commentCount);

        Likes likes = item.getLikes();
        String likesCount = likes != null ?
                Integer.toString(likes.getCount()) : Integer.toString(0);
        tvLikesCount.setText(likesCount);

        if (mImageLoader != null && !TextUtils.isEmpty(thumbUrl)) {
            mImageLoader.load(ivThumb, thumbUrl, true);
        } else {
            ivThumb.setImageDrawable(null);
        }

        setBtnLike(item, btnLike);

        loadMoreItems(position);

        return convertView;
    }

    private void setBtnLike(final Feed item, final View btnLike) {
        final boolean userHasLiked = item.isUserLiked();
        btnLike.setSelected(userHasLiked);
        final Likes likes = item.getLikes();
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFeedback(item, userHasLiked);
                item.setUserLiked(!userHasLiked);
                likes.setCount(likes.getCount() + (userHasLiked ? -1 : + 1));
                setBtnLike(item, btnLike);
            }
        });
    }

    private void handleFeedback(Feed item, boolean userHasLiked) {
        String messageFormat = null;
        if (userHasLiked) {
            messageFormat  = UNLIKES;
            RequestProvider.postUnLikes(mContext, item.getId(), null, mFeedbackCallback);
        } else {
            messageFormat  = LIKES;
            RequestProvider.postLikes(mContext, item.getId(), null, mFeedbackCallback);
        }

        ToastManager.getInstance().show(
                mContext, String.format(messageFormat, item.getUser().getName()));
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

    public void loadMoreItems(int position){
        int max = getItems().size();
        if (max > 4 && position == max - 1) {
            if (mNeedMoreCallback != null) {
                mNeedMoreCallback.onNeedMore();
            }
        }
    }
}
