package com.tonyjs.hashtagram.io.model.insta;

import com.tonyjs.hashtagram.io.model.BaseObject;

import java.util.ArrayList;

/**
 * Created by junseon on 2014-05-17.
 */
public class InstaLikes extends BaseObject {
    private int count;
    private ArrayList<InstaUser> users;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<InstaUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<InstaUser> users) {
        this.users = users;
    }
}
