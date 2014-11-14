package com.orcpark.hashtagram.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.TextUtils;
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

/**
 * Created by orcpark on 14. 11. 9..
 */
public class InstaRecyclerAdapter extends BasicRecyclerAdapter<InstaItem> {
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

    public InstaRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == Type.FOOTER.ordinal()) {
            return new InstaFooterViewHolder(mContext, inflater.inflate(R.layout.layout_footer, viewGroup, false));
        } else {
            return new InstaViewHolder(mContext, inflater.inflate(R.layout.recycler_item_insta, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(BasicViewHolder basicViewHolder, int position) {
        int max = getItemCount();

        if (max > 4 && position == max - 4) {
            if (mRequestMoreListener != null) {
                mRequestMoreListener.onRequestMore();
            }
        }

        if (basicViewHolder instanceof InstaFooterViewHolder) {
            ((InstaFooterViewHolder) basicViewHolder).itemView.setVisibility(
                    max > 1 ? View.VISIBLE : View.GONE
            );
            return;
        }

        InstaViewHolder holder = (InstaViewHolder) basicViewHolder;
        Item item = getItem(position);

        holder.tvCreatedTime.setText(TimeUtils.getTimeUntilMinuteWithHyphen(item.getCreateTime()));
        String summary = item.getCaption() != null ? item.getCaption().getTitle() : null;
        if (!TextUtils.isEmpty(summary)) {
            holder.tvSummary.setText(summary);
        }
        InstaImageInfo info = item.getImageInfo();
        InstaImageSpec spec = info != null ? info.getStandard() : null;
        final String thumbUrl = spec != null ? spec.getUrl() : null;
        final ImageView ivThumb = holder.ivThumb;

        InstaUser user = item.getUser();
        if (user != null) {
            String authorUrl = user.getProfilePictureUrl();
            ImageLoader.load(mContext, authorUrl, holder.ivAuthor, true);
            holder.tvAuthor.setText(user.getName());
        }

        InstaComment comment = item.getComments();
        String commentCount = comment != null ? Integer.toString(comment.getCount()) : Integer.toString(0);
        holder.tvComments.setText(commentCount);

        if (!item.isAnimated()) {
            View itemView = holder.itemView;
            AnimationUtils.startSoftlyShowAnimation(itemView, 300,
                    new AnimationUtils.OnAnimtaionEndListener() {
                        @Override
                        public void onAnimationEnd() {
                             ImageLoader.load(mContext, thumbUrl, ivThumb, true);
                        }
                    });
            item.setAnimated(true);
        } else {
            ImageLoader.load(mContext, thumbUrl, ivThumb, true);
        }

        setItemClickListener(holder.itemView, item);
    }

    @Override
    public void setItems(ArrayList<InstaItem> items) {
        super.setItems(getItems(items));
    }

    @Override
    public void addItems(ArrayList<InstaItem> items) {
        super.addItems(getItems(items));
    }

    @Override
    public void addItem(InstaItem item) {
        super.addItem(Item.getItem(item));
    }

    private ArrayList<InstaItem> getItems(ArrayList<InstaItem> items) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        int max = items.size();
        for (int i = 0; i < max; i++) {
            InstaItem item = items.get(i);
            items.set(i, Item.getItem(item));
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public Item getItem(int position) {
        if (position == getItemCount() - 1) {
            return null;
        }
        return (Item) super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? Type.FOOTER.ordinal() : Type.ITEM.ordinal();
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
    }

    private class InstaFooterViewHolder extends BasicViewHolder<Object> {

        public InstaFooterViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    public static class Item extends InstaItem {
        private boolean animated;

        public boolean isAnimated() {
            return animated;
        }

        public void setAnimated(boolean animated) {
            this.animated = animated;
        }

        public static Item getItem(InstaItem instaItem) {
            Item item = new Item();
            item.setTitle(instaItem.getTitle());
            item.setAttribution(instaItem.getAttribution());
            item.setId(instaItem.getId());
            item.setType(instaItem.getType());
            item.setCaption(instaItem.getCaption());
            item.setComments(instaItem.getComments());
            item.setCreateTime(instaItem.getCreateTime());
            item.setImageInfo(instaItem.getImageInfo());
            item.setLikes(instaItem.getLikes());
            item.setUser(instaItem.getUser());
            return item;
        }
    }

}
