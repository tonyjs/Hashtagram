package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony.park on 14. 11. 28..
 */
public abstract class MultiRecyclerAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    public MultiRecyclerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    private List<Row> mRows = new ArrayList<>();

    public void setRows(List<Row> rows){
        mRows = rows;
        notifyDataSetChanged();
    }

    public void addRows(List<Row> rows){
        mRows.addAll(rows);
        notifyDataSetChanged();
    }

    public void addRowsOnTop(List<Row> rows){
        mRows.addAll(0, rows);
        notifyDataSetChanged();
    }

    public void addRowOnTop(Object item, int viewType) {
        mRows.add(0, new Row(item, viewType));
        notifyItemInserted(0);
    }

    public void addRow(Object item, int viewType) {
        mRows.add(new Row(item, viewType));
        notifyItemInserted(mRows.size() - 1);
    }

    public void removeRow(int position) {
        int max = getItemCount();
        if (max > position) {
            mRows.remove(position);
            notifyItemRemoved(position);
        }
    }

    public List<Row> getRows() {
        return mRows;
    }

    protected Row getRow(Object item, int viewType) {
        return new Row(item, viewType);
    }

    @Override
    public int getItemCount() {
        return mRows != null ? mRows.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    public abstract BasicViewHolder getViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        holder.onBindView(getItem(position), position);
    }

    public Object getItem(int position) {
        return getItemCount() > position ? mRows.get(position).getItem() : null;
    }

    @Override
    public int getItemViewType(int position) {
        return mRows.get(position).getItemViewType();
    }


    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    public static class Row {
        private Object item;
        private int itemViewType;

        public Row(Object item, int viewType) {
            this.item = item;
            this.itemViewType = viewType;
        }

        public Object getItem() {
            return item;
        }

        public void setItem(Object item) {
            this.item = item;
        }

        public int getItemViewType() {
            return itemViewType;
        }

        public void setItemViewType(int itemViewType) {
            this.itemViewType = itemViewType;
        }
    }
}
