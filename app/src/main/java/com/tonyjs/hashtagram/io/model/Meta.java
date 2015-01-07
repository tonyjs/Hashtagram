package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class Meta extends BaseObject {
    @SerializedName("code") private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "code=" + code +
                '}';
    }
}
