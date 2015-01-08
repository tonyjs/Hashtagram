package com.tonyjs.hashtagram.io.request.retrofit;

import com.google.gson.annotations.SerializedName;
import com.tonyjs.hashtagram.io.model.HostInfo;

/**
 * Created by tonyjs on 15. 1. 8..
 */
public class HostInfoResponse extends Response {
    @SerializedName("data") private HostInfo hostInfo;

    public HostInfo getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(HostInfo hostInfo) {
        this.hostInfo = hostInfo;
    }

    @Override
    public String toString() {
        return "NewsFeedResponse{" +
                ", meta=" + getMeta() +
                ", data=" + hostInfo +
                '}';
    }
}
