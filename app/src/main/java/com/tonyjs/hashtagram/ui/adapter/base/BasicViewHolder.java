package com.tonyjs.hashtagram.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tony.park on 14. 11. 6..
 */
public abstract class BasicViewHolder<T> extends RecyclerView.ViewHolder{
    private Context mContext;
    public BasicViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract void onBindView(final T item);
}
