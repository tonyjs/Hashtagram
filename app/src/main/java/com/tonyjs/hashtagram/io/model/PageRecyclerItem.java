package com.tonyjs.hashtagram.io.model;

import android.support.v4.app.Fragment;
import com.tonyjs.hashtagram.ui.RecyclerFragment;

/**
 * Created by orcpark on 14. 11. 11..
 */
public class PageRecyclerItem extends PageItem {
    private String hashTag;
    private RecyclerFragment fragment;

    public PageRecyclerItem(int position, String hashTag) {
        this.hashTag = hashTag;
        fragment = RecyclerFragment.newInstance(position, hashTag);
    }

    public String getHashTag() {
        return hashTag;
    }

    public Fragment getFragment() {
        return fragment;
    }

}
