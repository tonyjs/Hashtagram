package com.tonyjs.hashtagram.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.tonyjs.hashtagram.ui.adapter.base.BasicRecyclerAdapter;
import com.tonyjs.hashtagram.ui.adapter.base.BasicViewHolder;
import com.tonyjs.hashtagram.ui.adapter.base.SparseViewHolder;
import com.tonyjs.hashtagram.ui.widget.GradientSquareImageView;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.TimeUtils;
import com.tonyjs.hashtagram.util.ToastManager;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class TimeLineRecyclerAdapter extends BasicRecyclerAdapter<Feed> {
    public interface NeedMoreCallback {
        public void onNeedMore();
    }

    private NeedMoreCallback mNeedMoreCallback;

    public void setNeedMoreCallback(NeedMoreCallback needMoreCallback) {
        mNeedMoreCallback = needMoreCallback;
    }

    public enum Type {
        FOOTER, ITEM
    }

    public TimeLineRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public BasicViewHolder getViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();
        if (viewType == Type.FOOTER.ordinal()) {
            return new FooterViewHolder(
                    getContext(), inflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new FeedViewHolder(
                    getContext(), inflater.inflate(R.layout.recycler_item_insta, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BasicViewHolder basicViewHolder, int position) {
        super.onBindViewHolder(basicViewHolder, position);

        int max = getItems().size();
        if (max > 4 && position == max - 1) {
            if (mNeedMoreCallback != null) {
                mNeedMoreCallback.onNeedMore();
            }
        }
    }

    private boolean mCanShowingProgress = true;

    public void removeProgress() {
        mCanShowingProgress = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (!mCanShowingProgress) {
            return super.getItemCount();
        }
        int itemCount = super.getItemCount() + 1;
        if (itemCount < 2) {
            itemCount = 0;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mCanShowingProgress) {
            return Type.ITEM.ordinal();
        }
        int size = getItems().size();
        return position == size ? Type.FOOTER.ordinal() : Type.ITEM.ordinal();
    }

    private class FeedViewHolder extends BasicViewHolder<Feed>{
        public GradientSquareImageView ivThumb;
        public TextView tvSummary;
        public ImageView ivAuthor;
        public TextView tvAuthor;
        public TextView tvCreatedTime;
        public TextView tvComments;
        public TextView tvLikesCount;
        public View btnLike;

        public FeedViewHolder(Context context, View itemView) {
            super(context, itemView);
            ivThumb = SparseViewHolder.get(itemView, R.id.iv_thumb);
            tvSummary = SparseViewHolder.get(itemView, R.id.tv_summary);
            ivAuthor = SparseViewHolder.get(itemView, R.id.iv_author);
            tvAuthor = SparseViewHolder.get(itemView, R.id.tv_author);
            tvCreatedTime = SparseViewHolder.get(itemView, R.id.tv_create_time);
            tvComments = SparseViewHolder.get(itemView, R.id.tv_comment_count);
            tvLikesCount = SparseViewHolder.get(itemView, R.id.tv_likes_count);
            btnLike = SparseViewHolder.get(itemView, R.id.btn_like);
        }

        @Override
        public void onBindView(Feed item) {
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
                ImageLoader.loadCircleDrawable(getContext(), authorUrl, ivAuthor);
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

            ImageLoader.load(getContext(), thumbUrl, ivThumb, true);
            setBtnLike(item);
        }

        private void setBtnLike(final Feed item) {
            final boolean userHasLiked = item.isUserLiked();
            btnLike.setSelected(userHasLiked);
            final Likes likes = item.getLikes();
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleFeedback(item, userHasLiked);
                    item.setUserLiked(!userHasLiked);
                    likes.setCount(likes.getCount() + (userHasLiked ? -1 : + 1));
                    notifyItemChanged(getPosition());
                }
            });
        }

        private void handleFeedback(Feed item, boolean userHasLiked) {
            String messageFormat = null;
            if (userHasLiked) {
                messageFormat  = "%s 님의 게시물의 좋아요를 취소합니다.";
                RequestProvider.postUnLikes(getContext(), item.getId(), null, mFeedbackCallback);
            } else {
                messageFormat  = "%s 님의 게시물을 좋아합니다.";
                RequestProvider.postLikes(getContext(), item.getId(), null, mFeedbackCallback);
            }

            ToastManager.getInstance().show(
                    getContext(), String.format(messageFormat, item.getUser().getName()));
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
    }

    private class FooterViewHolder extends BasicViewHolder<Object> {

        public FooterViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

}
