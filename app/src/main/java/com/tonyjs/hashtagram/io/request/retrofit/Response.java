package com.tonyjs.hashtagram.io.request.retrofit;

import com.google.gson.annotations.SerializedName;
import com.tonyjs.hashtagram.io.model.Meta;

/**
 * Created by tonyjs on 15. 1. 8..
 */
public class Response {
    @SerializedName("meta") private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
