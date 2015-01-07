package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class Comment extends BaseObject {
    @SerializedName("created_time") private String createdTime;
    @SerializedName("text") private String text;
    @SerializedName("id") private String id;
    @SerializedName("from") private User from;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "createdTime='" + createdTime + '\'' +
                ", text='" + text + '\'' +
                ", id='" + id + '\'' +
                ", from=" + from +
                '}';
    }
}
