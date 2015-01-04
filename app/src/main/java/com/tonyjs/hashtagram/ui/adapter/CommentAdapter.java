package com.tonyjs.hashtagram.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.model.insta.InstaCommentItem;
import com.tonyjs.hashtagram.io.model.insta.InstaUser;
import com.tonyjs.hashtagram.util.ImageLoader;

/**
 * Created by orcpark on 14. 11. 16..
 */
public class CommentAdapter extends BasicAdapter<InstaCommentItem> {
    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_comment, parent, false);
        }

        ImageView ivProfile = SparseViewHolder.get(convertView, R.id.iv_thumb);
        TextView tvName = SparseViewHolder.get(convertView, R.id.tv_name);
        TextView tvComment = SparseViewHolder.get(convertView, R.id.tv_comment);

        InstaCommentItem item = getItem(position);
        tvComment.setText(item.getText());

        InstaUser user = item.getFrom();
        String profileUrl = user != null ? user.getProfilePictureUrl() : null;
        ImageLoader.loadCircleDrawable(mContext, profileUrl, ivProfile);
//        ImageLoader.loadWithCallback(mContext, profileUrl,
//                new SimpleTarget<Bitmap>(ivProfile.getWidth(), ivProfile.getWidth()){
//
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        ivProfile.setImageDrawable(new RoundedDrawable(resource));
//                    }
//                });

        String name = user != null ? user.getFullName() : null;
        tvName.setText(name);

        return convertView;
    }
}
