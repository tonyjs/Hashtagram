package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.*;
import com.orcpark.hashtagram.io.request.ResponseListener;
import com.orcpark.hashtagram.ui.widget.GradientSquareImageView;
import com.orcpark.hashtagram.util.ImageLoader;
import com.orcpark.hashtagram.util.RequestFactory;
import com.orcpark.hashtagram.util.TimeUtils;

import org.json.JSONObject;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class TimeLineRecyclerAdapter extends BasicRecyclerAdapter<InstaItem> {
    public interface RequestMoreListener {
        public void onRequestMore();
    }

    private RequestMoreListener mRequestMoreListener;

    public void setRequestMoreListener(RequestMoreListener requestMoreListener) {
        mRequestMoreListener = requestMoreListener;
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
            return new InstaFooterViewHolder(
                    getContext(), inflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new InstaViewHolder(
                    getContext(), inflater.inflate(R.layout.recycler_item_insta, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BasicViewHolder basicViewHolder, int position) {
        basicViewHolder.onBindView(getItem(position));

        int max = getItems().size();
        if (max > 4 && position == max - 1) {
            if (mRequestMoreListener != null) {
                mRequestMoreListener.onRequestMore();
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

    private class InstaViewHolder extends BasicViewHolder<InstaItem>{
        public GradientSquareImageView ivThumb;
        public TextView tvSummary;
//        public CircleImageView ivAuthor;
        public ImageView ivAuthor;
        public TextView tvAuthor;
        public TextView tvCreatedTime;
        public TextView tvComments;
        public TextView tvLikesCount;
        public View btnLike;

        public InstaViewHolder(Context context, View itemView) {
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
        public void onBindView(InstaItem item) {
            tvCreatedTime.setText(TimeUtils.getRelativeTime(item.getCreateTime()));

            String summary = item.getCaption() != null ? item.getCaption().getTitle() : "";
            tvSummary.setText(summary);

            InstaImageInfo info = item.getImageInfo();
            InstaImageSpec spec = info != null ? info.getStandard() : null;
            final String thumbUrl = spec != null ? spec.getUrl() : null;
            InstaUser user = item.getUser();
            if (user != null) {
                String authorUrl = user.getProfilePictureUrl();
//                ImageLoader.load(getContext(), authorUrl, ivAuthor, true);
                ImageLoader.loadCircleDrawable(getContext(), authorUrl, ivAuthor);
//                ivAuthor.setImageUrl(authorUrl);
//                ImageLoader.loadWithCallback(getContext(), authorUrl,
//                        new SimpleTarget<Bitmap>(160,160){
//
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                                final Bitmap output = Bitmap.createBitmap(
//                                        resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);
//                                final Canvas canvas = new Canvas(output);
//
//                                final int color = Color.TRANSPARENT;
//                                final Paint paint = new Paint();
//                                final Rect rect = new Rect(0, 0, resource.getWidth(), resource.getHeight());
//                                final RectF rectF = new RectF(rect);
//
//                                paint.setAntiAlias(true);
//                                canvas.drawARGB(0, 0, 0, 0);
//                                paint.setColor(color);
//                                canvas.drawOval(rectF, paint);
//
//                                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//                                canvas.drawBitmap(resource, rect, rect, paint);
//
////                                resource.recycle();
//
////                                ivAuthor.setImageBitmap(output);
//                                ivAuthor.setImageBitmap(resource);
//                            }
//                        });
                tvAuthor.setText(user.getName());
            }

            InstaComment comment = item.getComments();
            String commentCount = comment != null ?
                    Integer.toString(comment.getCount()) : Integer.toString(0);
            tvComments.setText(commentCount);

            InstaLikes likes = item.getLikes();
            String likesCount = likes != null ?
                    Integer.toString(likes.getCount()) : Integer.toString(0);
            tvLikesCount.setText(likesCount);

            ImageLoader.load(getContext(), thumbUrl, ivThumb, true);
            setBtnLike(item);
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

        private void setBtnLike(final InstaItem item) {
            final boolean userHasLiked = item.userHasLiked();
            btnLike.setSelected(userHasLiked);
            final InstaLikes likes = item.getLikes();
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleFeedback(item, userHasLiked);
                    item.setUserHasLiked(!userHasLiked);
                    likes.setCount(likes.getCount() + (userHasLiked ? -1 : + 1));
                    notifyItemChanged(getPosition());
                }
            });
        }

        private void handleFeedback(InstaItem item, boolean userHasLiked) {
            String messageFormat = null;
            if (userHasLiked) {
                messageFormat  = "%s 님의 게시물의 좋아요를 취소합니다.";
                RequestFactory.postUnLike(getContext(), item.getId(), null, mFeedbackCallback);
            } else {
                messageFormat  = "%s 님의 게시물을 좋아합니다.";
                RequestFactory.postLike(getContext(), item.getId(), null, mFeedbackCallback);
            }

            Toast.makeText(getContext(),
                            String.format(messageFormat, item.getUser().getName()),
                            Toast.LENGTH_SHORT).show();
        }

    }

    private class InstaFooterViewHolder extends BasicViewHolder<Object> {

        public InstaFooterViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

}
