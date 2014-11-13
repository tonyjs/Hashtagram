package com.orcpark.hashtagram.io.model.insta;

import com.orcpark.hashtagram.io.model.BaseObject;

/**
 * Created by JunSeon Park on 2014-04-04.
 */
public class InstaImageInfo extends BaseObject {
    private InstaImageSpec low;
    private InstaImageSpec thumbnail;
    private InstaImageSpec standard;

    public InstaImageSpec getLow() {
        return low;
    }

    public void setLow(InstaImageSpec low) {
        this.low = low;
    }

    public InstaImageSpec getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(InstaImageSpec thumbnail) {
        this.thumbnail = thumbnail;
    }

    public InstaImageSpec getStandard() {
        return standard;
    }

    public void setStandard(InstaImageSpec standard) {
        this.standard = standard;
    }
}
