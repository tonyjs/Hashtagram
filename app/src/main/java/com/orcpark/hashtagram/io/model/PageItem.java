package com.orcpark.hashtagram.io.model;

import android.support.v4.app.Fragment;
import android.view.View;
import com.orcpark.hashtagram.ui.InstagramFragment;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class PageItem {
    private String hashTag;
    private InstagramFragment fragment;

    public PageItem() {
    }

    public PageItem(int position, String hashTag) {
        this.hashTag = hashTag;
        fragment = InstagramFragment.newInstance(position, hashTag);
    }

    public String getHashTag() {
        return hashTag;
    }

    public Fragment getFragment() {
        return fragment;
    }

}
