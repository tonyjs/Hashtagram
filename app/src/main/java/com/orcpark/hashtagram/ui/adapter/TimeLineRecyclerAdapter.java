package com.orcpark.hashtagram.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.*;
import com.orcpark.hashtagram.ui.widget.GradientSquareImageView;
import com.orcpark.hashtagram.util.AnimationUtils;
import com.orcpark.hashtagram.util.ImageLoader;
import com.orcpark.hashtagram.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

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

    private class InstaViewHolder extends BasicViewHolder<InstaItem> {
        public GradientSquareImageView ivThumb;
        public TextView tvSummary;
        public ImageView ivAuthor;
        public TextView tvAuthor;
        public TextView tvCreatedTime;
        public TextView tvComments;

        public InstaViewHolder(Context context, View itemView) {
            super(context, itemView);
            ivThumb = SparseViewHolder.get(itemView, R.id.iv_thumb);
            tvSummary = SparseViewHolder.get(itemView, R.id.tv_summary);
            ivAuthor = SparseViewHolder.get(itemView, R.id.iv_author);
            tvAuthor = SparseViewHolder.get(itemView, R.id.tv_author);
            tvCreatedTime = SparseViewHolder.get(itemView, R.id.tv_create_time);
            tvComments = SparseViewHolder.get(itemView, R.id.tv_comment_count);
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
                ImageLoader.load(getContext(), authorUrl, ivAuthor, true);
                tvAuthor.setText(user.getName());
            }

            InstaComment comment = item.getComments();
            String commentCount = comment != null ?
                    Integer.toString(comment.getCount()) : Integer.toString(0);
            tvComments.setText(commentCount);

            ImageLoader.load(getContext(), thumbUrl, ivThumb, true);
            if (!item.isAnimated()) {
                AnimationUtils.startSoftlySlideUp(itemView, 500);
                item.setAnimated(true);
            }
//            ImageLoader.load(getContext(), thumbUrl, ivThumb, true);
        }
    }

    private class InstaFooterViewHolder extends BasicViewHolder<Object> {

        public InstaFooterViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

}
