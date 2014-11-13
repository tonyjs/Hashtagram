package com.orcpark.hashtagram.io.model.insta;

import com.orcpark.hashtagram.io.model.BaseObject;

/**
 * Created by orcpark on 2014. 5. 25..
 */
public class InstaCommentItem extends BaseObject {
    private long createdTime;
    private String text;
    private InstaUser from;
    private String id;

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InstaUser getFrom() {
        return from;
    }

    public void setFrom(InstaUser from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
