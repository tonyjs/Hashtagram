package com.tonyjs.hashtagram.io.model;

import android.support.v4.app.Fragment;
import com.tonyjs.hashtagram.ui.ListFragment;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class PageItem {
    private boolean selected = false;
    private String hashTag;
    private ListFragment fragment;

    public PageItem() {
    }

    public PageItem(int position, String hashTag) {
        this.hashTag = hashTag;
        fragment = ListFragment.newInstance(position, hashTag);
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
