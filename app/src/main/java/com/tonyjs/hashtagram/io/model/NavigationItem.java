package com.tonyjs.hashtagram.io.model;

import android.support.v4.app.Fragment;

import com.tonyjs.hashtagram.ui.FeedListFragment;

/**
 * Created by orcpark on 14. 11. 11..
 */
public class NavigationItem {
    private boolean selected = false;
    private String hashTag;
    private FeedListFragment fragment;

    public NavigationItem(int position, String hashTag) {
        this.hashTag = hashTag;
        fragment = FeedListFragment.newInstance(position, hashTag);
    }

    public String getHashTag() {
        return hashTag;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
