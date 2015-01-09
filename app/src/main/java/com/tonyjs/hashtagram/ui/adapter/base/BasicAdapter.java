package com.tonyjs.hashtagram.ui.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by tony.park on 14. 11. 5..
 */
public abstract class BasicAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    public BasicAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    protected ArrayList<T> mItems;
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

    public ArrayList<T> getItems() {
        return mItems;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        return getCount() > 0 ? mItems.get(position) : null;
    }

}
