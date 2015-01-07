package com.tonyjs.hashtagram.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public class User extends BaseObject{
    @SerializedName("username") private String name;
    @SerializedName("full_name") private String fullName;
    @SerializedName("id") private String id;
    @SerializedName("profile_picture") private String profileImageUrl;
    @SerializedName("bio") private String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id='" + id + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
