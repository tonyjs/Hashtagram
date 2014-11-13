package com.orcpark.hashtagram.io.model;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by JunSeon Park on 2014-04-04.
 */
public class BaseObject extends Observable implements Serializable {
    public static final long serialVersionUID = 1000L;

    private String title;

    public BaseObject() {
    }

    public BaseObject(String title) {
        this.title = title;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void update(){
        setChanged();
        notifyObservers(this);
    }

}
