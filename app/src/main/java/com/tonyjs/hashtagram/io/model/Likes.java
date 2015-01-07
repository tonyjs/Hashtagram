package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class Likes extends BaseObject {
    @SerializedName("count") private int count;
    @SerializedName("data") private ArrayList<User> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Likes{" +
                "count=" + count +
                ", data=" + data +
                '}';
    }
}
