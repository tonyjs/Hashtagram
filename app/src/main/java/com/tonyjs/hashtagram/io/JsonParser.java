package com.tonyjs.hashtagram.io;

import com.tonyjs.hashtagram.io.model.insta.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 9. 6..
 */
public class JsonParser {

    public static Instagram getInstagram(JSONObject jsonObject) {
        if (jsonObject != null) {
            Instagram instagram = new Instagram();
            JSONObject objPagination = jsonObject.optJSONObject("pagination");
            if (objPagination != null) {
                instagram.setNextUrl(objPagination.optString("next_url"));
            }
            JSONArray arrData = jsonObject.optJSONArray("data");
            instagram.setInstaItems(getInstaItem(arrData));
            return instagram;
        }
        return null;
    }

    public static ArrayList<InstaItem> getInstaItem(JSONArray data) {
        if(data != null){
            int max = data.length();
            if(max > 0) {
                ArrayList<InstaItem> items = new ArrayList<InstaItem>();
                for (int i = 0; i < max; i++) {
                    JSONObject objItem = data.optJSONObject(i);
                    if (objItem != null) {
                        InstaItem item = new InstaItem();
                        item.setId(objItem.optString("id"));
                        item.setAttribution(objItem.optString("attribution"));
                        item.setType(objItem.optString("type"));
                        item.setCreateTime(objItem.optLong("created_time"));
                        JSONObject objImageInfo = objItem.optJSONObject("images");
                        if (objImageInfo != null) {
                            InstaImageInfo imageInfo = new InstaImageInfo();
                            JSONObject objLow = objImageInfo.optJSONObject("low_resolution");
                            if (objLow != null) {
                                InstaImageSpec low = new InstaImageSpec();
                                low.setUrl(objLow.optString("url"));
                                low.setWidth(objLow.optInt("width"));
                                low.setHeight(objLow.optInt("height"));
                                imageInfo.setLow(low);
                            }
                            JSONObject objThumbnail = objImageInfo.optJSONObject("thumbnail");
                            if (objThumbnail != null) {
                                InstaImageSpec thumb = new InstaImageSpec();
                                thumb.setUrl(objThumbnail.optString("url"));
                                thumb.setWidth(objThumbnail.optInt("width"));
                                thumb.setHeight(objThumbnail.optInt("height"));
                                imageInfo.setThumbnail(thumb);
                            }
                            JSONObject objStandard = objImageInfo.optJSONObject("standard_resolution");
                            if (objStandard != null) {
                                InstaImageSpec standard = new InstaImageSpec();
                                standard.setUrl(objStandard.optString("url"));
                                standard.setWidth(objStandard.optInt("width"));
                                standard.setHeight(objStandard.optInt("height"));
                                imageInfo.setStandard(standard);
                            }
                            item.setImageInfo(imageInfo);
                        }

                        JSONObject objCaption = objItem.optJSONObject("caption");
                        if (objCaption != null) {
                            InstaCaption caption = new InstaCaption();
                            caption.setTitle(objCaption.optString("text"));
                            item.setCaption(caption);
                        }

                        JSONObject objFrom = objItem.optJSONObject("user");
                        if (objFrom != null) {
                            InstaUser user = new InstaUser();
                            user.setName(objFrom.optString("username"));
                            user.setFullName(objFrom.optString("full_name"));
                            user.setProfilePictureUrl(objFrom.optString("profile_picture"));
                            user.setId(objFrom.optString("id"));
                            user.setBio(objFrom.optString("bio"));
                            item.setUser(user);
                        }

                        JSONObject objComment = objItem.optJSONObject("comments");
                        item.setComments(getInstaCommentFromInnerObject(objComment));

                        JSONObject objLikes = objItem.optJSONObject("likes");
                        item.setLikes(getInstaLikesFromInnerObject(objLikes));

                        item.setUserHasLiked(objItem.optBoolean("user_has_liked"));

                        items.add(item);
                    }
                }
                return items;
            }
        }
        return null;
    }

    public static InstaLikes getInstaLikesFromInnerObject(JSONObject objInstaLikes) {
        if (objInstaLikes != null) {
            InstaLikes instaLikes = new InstaLikes();
            instaLikes.setCount(objInstaLikes.optInt("count"));
            instaLikes.setUsers(getInstaUserListFromInnerArray(objInstaLikes.optJSONArray("data")));
            return instaLikes;
        }
        return null;
    }

    public static ArrayList<InstaUser> getInstaUserListFromInnerArray(JSONArray arrUser) {
        if (arrUser != null) {
            int max = arrUser.length();
            if (max > 0) {
                ArrayList<InstaUser> items = new ArrayList<InstaUser>();
                for (int i = 0; i < max; i++) {
                    JSONObject objUser = arrUser.optJSONObject(i);
                    if (objUser != null) {
                        items.add(getInstaUserFromInnerObject(objUser));
                    }
                }
                return items;
            }
        }
        return null;
    }

    public static InstaUser getInstaUserFromInnerObject(JSONObject objUser) {
        if (objUser != null) {
            InstaUser user = new InstaUser();
            user.setName(objUser.optString("username"));
            user.setFullName(objUser.optString("full_name"));
            user.setProfilePictureUrl(objUser.optString("profile_picture"));
            user.setId(objUser.optString("id"));
            user.setBio(objUser.optString("bio"));
            return user;
        }
        return null;
    }

    public static InstaComment getInstaCommentFromInnerObject(JSONObject objComment) {
        if (objComment != null) {
            InstaComment comment = new InstaComment();
            comment.setCount(objComment.optInt("count"));
            JSONArray arrData = objComment.optJSONArray("data");
            if (arrData != null) {
                int max = arrData.length();
                if (max > 0) {
                    ArrayList<InstaCommentItem> comments = new ArrayList<InstaCommentItem>();
                    for (int i = 0; i < max; i++) {
                        JSONObject objInstaComment = arrData.optJSONObject(i);
                        if (objInstaComment != null) {
                            comments.add(getInstaCommentItemFromInnerObject(objInstaComment));
                        }
                    }
                    comment.setItems(comments);
                }
            }
            return comment;
        }
        return null;
    }

    public static InstaCommentItem getInstaCommentItemFromInnerObject(JSONObject objInstaComment) {
        if (objInstaComment != null) {
            InstaCommentItem comment = new InstaCommentItem();
            comment.setId(objInstaComment.optString("id"));
            comment.setCreatedTime(objInstaComment.optLong("created_time"));
            comment.setText(objInstaComment.optString("text"));
            comment.setFrom(getInstaUserFromInnerObject(objInstaComment.optJSONObject("from")));
            return comment;
        }

        return null;
    }

    public static UserInfo getUserInfo(JSONObject objUser) {
        if (objUser != null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAccessToken(objUser.optString("access_token"));
            userInfo.setUser(getInstaUserFromInnerObject(objUser.optJSONObject("user")));
            return userInfo;
        }
        return null;
    }
}
