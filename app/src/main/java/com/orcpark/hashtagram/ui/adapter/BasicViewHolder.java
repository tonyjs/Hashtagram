package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tony.park on 14. 11. 6..
 */
public abstract class BasicViewHolder<T> extends RecyclerView.ViewHolder {

    protected Context mContext;
    public BasicViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
    }

    public void onBindView(T item, int position){};
}
