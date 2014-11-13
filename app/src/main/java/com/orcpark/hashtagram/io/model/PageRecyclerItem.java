package com.orcpark.hashtagram.io.model;

import android.support.v4.app.Fragment;
import com.orcpark.hashtagram.ui.InstagramFragment;
import com.orcpark.hashtagram.ui.InstagramRecyclerFragment;

/**
 * Created by orcpark on 14. 11. 11..
 */
public class PageRecyclerItem extends PageItem{
    private String hashTag;
    private InstagramRecyclerFragment fragment;

    public PageRecyclerItem(int position, String hashTag) {
        this.hashTag = hashTag;
        fragment = InstagramRecyclerFragment.newInstance(position, hashTag);
    }

    public String getHashTag() {
        return hashTag;
    }

    public Fragment getFragment() {
        return fragment;
    }

}
