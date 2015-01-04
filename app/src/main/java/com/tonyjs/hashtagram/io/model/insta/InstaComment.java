package com.tonyjs.hashtagram.io.model.insta;

import com.tonyjs.hashtagram.io.model.BaseObject;
import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 6. 3..
 */
public class InstaComment extends BaseObject {
    private int count;
    private ArrayList<InstaCommentItem> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<InstaCommentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<InstaCommentItem> items) {
        this.items = items;
    }
}
