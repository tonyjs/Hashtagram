package com.tonyjs.hashtagram.io.request.retrofit;

import com.google.gson.annotations.SerializedName;
import com.tonyjs.hashtagram.io.model.Feed;
import com.tonyjs.hashtagram.io.model.Meta;
import com.tonyjs.hashtagram.io.model.Pagination;

import java.util.ArrayList;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class NewsFeedResponse {
    @SerializedName("pagination") private Pagination pagination;
    @SerializedName("meta") private Meta meta;
    @SerializedName("data") private ArrayList<Feed> data;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public ArrayList<Feed> getData() {
        return data;
    }

    public void setData(ArrayList<Feed> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NewsFeedResponse{" +
                "pagination=" + pagination +
                ", meta=" + meta +
                ", data=" + data +
                '}';
    }
}
