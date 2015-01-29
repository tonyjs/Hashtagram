package com.tonyjs.hashtagram.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.model.Comment;
import com.tonyjs.hashtagram.io.model.User;
import com.tonyjs.hashtagram.ui.adapter.base.BasicAdapter;
import com.tonyjs.hashtagram.ui.adapter.base.SparseViewHolder;
import com.tonyjs.hashtagram.util.ImageLoader;
import com.tonyjs.hashtagram.util.ImageLoaderOld;

/**
 * Created by orcpark on 14. 11. 16..
 */
public class CommentAdapter extends BasicAdapter<Comment> {
    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_comment, parent, false);
        }

        ImageView ivProfile = SparseViewHolder.get(convertView, R.id.iv_thumb);
        TextView tvName = SparseViewHolder.get(convertView, R.id.tv_name);
        TextView tvComment = SparseViewHolder.get(convertView, R.id.tv_comment);

        Comment item = getItem(position);
        tvComment.setText(item.getText());

        User user = item.getFrom();
        String profileUrl = user != null ? user.getProfileImageUrl() : null;
        if (!TextUtils.isEmpty(profileUrl)) {
            ImageLoader.load(mContext, ivProfile, profileUrl, ImageLoader.TransformationType.CIRCLE);
        } else {
            ivProfile.setImageDrawable(null);
        }
        String name = user != null ? user.getFullName() : null;
        tvName.setText(name);

        return convertView;
    }
}
