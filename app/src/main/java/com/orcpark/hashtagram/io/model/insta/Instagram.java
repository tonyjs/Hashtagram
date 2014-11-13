package com.orcpark.hashtagram.io.model.insta;

import java.util.ArrayList;

/**
 * Created by junseon on 2014-04-15.
 */
public class Instagram {
    private ArrayList<InstaItem> instaItems;
    private String nextUrl;

    public ArrayList<InstaItem> getInstaItems() {
        return instaItems;
    }

    public void setInstaItems(ArrayList<InstaItem> instaItems) {
        this.instaItems = instaItems;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
