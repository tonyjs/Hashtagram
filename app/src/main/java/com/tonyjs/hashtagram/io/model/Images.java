package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public class Images extends BaseObject{
    @SerializedName("low_resolution") private ImageResolution low;
    @SerializedName("thumbnail") private ImageResolution thumbnail;
    @SerializedName("standard_resolution") private ImageResolution standard;

    public ImageResolution getLow() {
        return low;
    }

    public void setLow(ImageResolution low) {
        this.low = low;
    }

    public ImageResolution getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageResolution thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageResolution getStandard() {
        return standard;
    }

    public void setStandard(ImageResolution standard) {
        this.standard = standard;
    }

    @Override
    public String toString() {
        return "Images{" +
                "low=" + low +
                ", thumbnail=" + thumbnail +
                ", standard=" + standard +
                '}';
    }
}
