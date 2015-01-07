package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class Comments extends BaseObject {
    @SerializedName("count") private int count;
    @SerializedName("data") private ArrayList<Comment> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Comment> getData() {
        return data;
    }

    public void setData(ArrayList<Comment> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "count=" + count +
                ", data=" + data +
                '}';
    }
}
