package com.tonyjs.hashtagram.io.model.insta;

import com.tonyjs.hashtagram.io.model.BaseObject;

/**
 * Created by JunSeon Park on 2014-04-04.
 */
public class InstaImageSpec extends BaseObject {
    private String url;
    private int width;
    private int height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
