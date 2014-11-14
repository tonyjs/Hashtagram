package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by tony.park on 14. 11. 5..
 */
public abstract class BasicRecyclerAdapter<T>
                        extends RecyclerView.Adapter<BasicViewHolder> {
    public interface OnItemClickListener {
        public void onItemClick(Object item);
    }
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

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BasicViewHolder basicViewHolder, int i) {
        final T item = getItem(i);
        basicViewHolder.onBindView(item, i);
        setItemClickListener(basicViewHolder.itemView, item);
    }

    public void setItemClickListener(View view, final T item) {
        if (mOnItemClickListener == null) {
            return;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public T getItem(int position) {
        return getItemCount() > 0 ? mItems.get(position) : null;
    }

}
