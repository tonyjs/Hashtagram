package com.tonyjs.hashtagram.io.model.insta;

import com.tonyjs.hashtagram.io.model.BaseObject;

/**
 * Created by tony.park on 15. 1. 4..
 */
public class UserInfo extends BaseObject{
    private String accessToken;
    private InstaUser user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public InstaUser getUser() {
        return user;
    }

    public void setUser(InstaUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", user=" + user.getId() +
                '}';
    }
}
