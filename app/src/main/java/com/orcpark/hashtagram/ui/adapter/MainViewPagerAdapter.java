package com.orcpark.hashtagram.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.orcpark.hashtagram.io.model.PageItem;

import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public MainViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    private ArrayList<PageItem> mItems;

    public void setItems(ArrayList<PageItem> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public void addItem(PageItem item) {
        if (mItems == null) {
            mItems = new ArrayList<PageItem>();
        }

        mItems.add(item);
        notifyDataSetChanged();
    }

    public PageItem getPageItem(int position) {
        return mItems != null && mItems.size() > position ? mItems.get(position) : null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PageItem item = getPageItem(position);
        return item != null ? item.getHashTag() : null;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        PageItem item = getPageItem(position);
        return item != null ? item.getFragment() : null;
    }
}
