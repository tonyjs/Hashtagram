package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class Pagination extends BaseObject{
    @SerializedName("next_url") private String nextUrl;
    @SerializedName("next_max_id") private String nextMaxId;

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getNextMaxId() {
        return nextMaxId;
    }

    public void setNextMaxId(String nextMaxId) {
        this.nextMaxId = nextMaxId;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "nextUrl='" + nextUrl + '\'' +
                ", nextMaxId='" + nextMaxId + '\'' +
                '}';
    }
}
