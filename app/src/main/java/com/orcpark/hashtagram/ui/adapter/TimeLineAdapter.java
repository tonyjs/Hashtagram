package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.*;
import com.orcpark.hashtagram.ui.widget.GradientNetworkImageView;
import com.orcpark.hashtagram.ui.widget.GradientSquareImageView;
import com.orcpark.hashtagram.util.ImageLoader;
import com.orcpark.hashtagram.util.TimeUtils;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class TimeLineAdapter extends BasicAdapter<InstaItem> {
    public interface RequestMoreListener {
        public void onRequestMore();
    }

    public TimeLineAdapter(Context context) {
        super(context);
    }

    private RequestMoreListener mRequestMoreListener;

    public void setRequestMoreListener(RequestMoreListener requestMoreListener) {
        mRequestMoreListener = requestMoreListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_insta, parent, false);
        }

        GradientNetworkImageView ivThumb = SparseViewHolder.get(convertView, R.id.iv_thumb);
        TextView tvSummary = SparseViewHolder.get(convertView, R.id.tv_summary);
        ImageView ivAuthor = SparseViewHolder.get(convertView, R.id.iv_author);
        TextView tvAuthor = SparseViewHolder.get(convertView, R.id.tv_author);
        TextView tvCreatedTime = SparseViewHolder.get(convertView, R.id.tv_create_time);
        final TextView tvComments = SparseViewHolder.get(convertView, R.id.tv_comment_count);

        InstaItem item = getItem(position);

        tvCreatedTime.setText(TimeUtils.getTimeUntilMinuteWithHyphen(item.getCreateTime()));
        String summary = item.getCaption() != null ? item.getCaption().getTitle() : null;
        if (!TextUtils.isEmpty(summary)) {
            tvSummary.setText(summary);
        }
        InstaImageInfo info = item.getImageInfo();
        InstaImageSpec spec = info != null ? info.getStandard() : null;
        String thumbnailUrl = spec != null ? spec.getUrl() : null;
        ImageLoader.load(mContext, thumbnailUrl, ivThumb);

        InstaUser user = item.getUser();
        if (user != null) {
            String authorUrl = user.getProfilePictureUrl();
            ImageLoader.load(mContext, authorUrl, ivAuthor);
            tvAuthor.setText(user.getName());
        }

        InstaComment comment = item.getComments();
        String commentCount = comment != null ? Integer.toString(comment.getCount()) : Integer.toString(0);
        tvComments.setText(commentCount);

       if (position == getCount() - 3) {
            if (mRequestMoreListener != null) {
                mRequestMoreListener.onRequestMore();
            }
        }
        return convertView;
    }
}
