package com.tonyjs.hashtagram.io.model;

import android.support.v4.app.Fragment;

import com.tonyjs.hashtagram.ui.FeedListFragment;
import com.tonyjs.hashtagram.ui.FeedRecyclerFragment;

/**
 * Created by orcpark on 14. 11. 11..
 */
public class NavigationItem_ {
    private boolean selected = false;
    private String hashTag;
    private FeedRecyclerFragment fragment;
//    private FeedListFragment fragment;

    public NavigationItem_(int position, String hashTag) {
        this.hashTag = hashTag;
        fragment = FeedRecyclerFragment.newInstance(position, hashTag);
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
