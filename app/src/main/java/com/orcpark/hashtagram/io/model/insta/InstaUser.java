package com.orcpark.hashtagram.io.model.insta;

import com.orcpark.hashtagram.io.model.BaseObject;

/**
 * Created by JunSeon Park on 2014-04-04.
 */
public class InstaUser extends BaseObject {

    private String name;
    private String profilePictureUrl;
    private String fullName;
    private String id;
    private String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
