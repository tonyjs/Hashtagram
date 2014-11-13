package com.orcpark.hashtagram.io.model.insta;

import com.orcpark.hashtagram.io.model.BaseObject;

/**
 * Created by JunSeon Park on 2014-04-04.
 */
public class InstaItem extends BaseObject {
    private String id;
    private String attribution;
    private String type;
    private InstaImageInfo imageInfo;
    private InstaUser user;
    private InstaCaption caption;
    private InstaLikes likes;
    private InstaComment comments;
    private boolean userHasLiked;
    private long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InstaImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(InstaImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public InstaUser getUser() {
        return user;
    }

    public void setUser(InstaUser user) {
        this.user = user;
    }

    public InstaCaption getCaption() {
        return caption;
    }

    public void setCaption(InstaCaption caption) {
        this.caption = caption;
    }

    public boolean isUserHasLiked() {
        return userHasLiked;
    }

    public void setUserHasLiked(boolean userHasLiked) {
        this.userHasLiked = userHasLiked;
    }

    public InstaLikes getLikes() {
        return likes;
    }

    public void setLikes(InstaLikes likes) {
        this.likes = likes;
    }

    public InstaComment getComments() {
        return comments;
    }

    public void setComments(InstaComment comments) {
        this.comments = comments;
    }
}
