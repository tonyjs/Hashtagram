package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonyjs on 15. 1. 8..
 */
public class HostInfo extends BaseObject {
    @SerializedName("access_token") private String accessToken;
    @SerializedName("user") private User user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "HostInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", user=" + user +
                '}';
    }
}
