package com.tonyjs.hashtagram.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
import com.tonyjs.hashtagram.ui.widget.GradientNetworkImageView;
import com.tonyjs.hashtagram.ui.widget.GradientSquareImageView;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.TimeUtils;
import com.tonyjs.hashtagram.util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class TimeLineListAdapterWithVolley extends TimeLineListAdapter {
    public TimeLineListAdapterWithVolley(Context context, ImageLoader imageLoader) {
        super(context, imageLoader);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_insta_with_volley, parent, false);
        }

        GradientNetworkImageView ivThumb = SparseViewHolder.get(convertView, R.id.iv_thumb);
        TextView tvSummary = SparseViewHolder.get(convertView, R.id.tv_summary);
        ImageView ivAuthor = SparseViewHolder.get(convertView, R.id.iv_author);
        TextView tvAuthor = SparseViewHolder.get(convertView, R.id.tv_author);
        TextView tvCreatedTime = SparseViewHolder.get(convertView, R.id.tv_create_time);
        TextView tvComments = SparseViewHolder.get(convertView, R.id.tv_comment_count);
        TextView tvLikesCount = SparseViewHolder.get(convertView, R.id.tv_likes_count);
        View btnLike = SparseViewHolder.get(convertView, R.id.btn_like);

        Feed item = getItem(position);

        ImageLoader imageLoader = getImageLoader();

        long createdTime = Long.valueOf(item.getCreatedTime());
        tvCreatedTime.setText(TimeUtils.getRelativeTime(createdTime));

        String summary = item.getCaption() != null ? item.getCaption().getText() : "";
        tvSummary.setText(summary);

        User user = item.getUser();
        if (user != null) {
            String authorUrl = user.getProfileImageUrl();
            if (imageLoader != null && !TextUtils.isEmpty(authorUrl)) {
                imageLoader.load(ivAuthor, authorUrl, ImageLoader.TransformationType.CIRCLE);
            } else {
                ivAuthor.setImageDrawable(null);
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

        Images info = item.getImages();
        ImageResolution spec = info != null ? info.getStandard() : null;
        ivThumb.setImageSpec(spec);

        setBtnLike(item, btnLike);

        loadMoreItems(position);

        animate(convertView, position);
        return convertView;
    }
}
