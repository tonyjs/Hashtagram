package com.tonyjs.hashtagram.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony.park on 14. 11. 5..
 */
public abstract class BasicRecyclerAdapter<T> extends RecyclerView.Adapter<BasicViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    public BasicRecyclerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public BasicRecyclerAdapter(Context context, List<T> items) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        setItems(items);
    }

    private List<T> mItems = new ArrayList<>();
    public void setItems(List<T> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItemsOnTop(List<T> items) {
        mItems.addAll(0, items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeItem(int position) {
        if (getItemCount() > position) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    public abstract BasicViewHolder getViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        holder.onBindView(getItem(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public T getItem(int position) {
        int max = mItems.size();
        return max > position ? mItems.get(position) : null;
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    public List<T> getItems() {
        return mItems;
    }

}
