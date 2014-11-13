package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by tony.park on 14. 11. 5..
 */
public abstract class BasicRecyclerAdapter<T>
                        extends RecyclerView.Adapter<BasicViewHolder> {

    protected Context mContext;

    public BasicRecyclerAdapter(Context context) {
        mContext = context;
    }

    private ArrayList<T> mItems;
    public void setItems(ArrayList<T> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<T> items) {
        if (mItems == null) {
            setItems(items);
            return;
        }

        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItemsOnTop(ArrayList<T> items) {
        if (mItems == null) {
            setItems(items);
            return;
        }

        mItems.addAll(0, items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (mItems == null) {
            mItems = new ArrayList<T>();
        }
        mItems.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BasicViewHolder basicViewHolder, int i) {
        basicViewHolder.onBindView(getItem(i), i);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public T getItem(int position) {
        return getItemCount() > 0 ? mItems.get(position) : null;
    }

}
