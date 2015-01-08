package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.tonyjs.hashtagram.config.PreferenceConfig;
import com.tonyjs.hashtagram.io.model.HostInfo;
import com.tonyjs.hashtagram.io.model.User;
import com.tonyjs.hashtagram.io.model.insta.InstaUser;
import com.tonyjs.hashtagram.io.model.insta.UserInfo;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class PrefUtils {

    public static boolean hasInstalled(Context context) {
        return getPreferences(context).getBoolean(PreferenceConfig.HAS_INSTALLED, false);
    }

    public static void setHasInstalled(Context context, boolean installed) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(PreferenceConfig.HAS_INSTALLED, installed);
        editor.commit();
    }

    public static String getAccessToken(Context context) {
        return getPreferences(context).getString(PreferenceConfig.ACCESS_TOKEN, null);
    }

    public static void setAccessToken(Context context, String accessToken) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(PreferenceConfig.ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public static InstaUser getUser(Context context) {
        SharedPreferences preferences = getPreferences(context);
        String accessToken = preferences.getString(PreferenceConfig.ACCESS_TOKEN, null);
        if (!TextUtils.isEmpty(accessToken)) {
            InstaUser user = new InstaUser();
            user.setId(preferences.getString(PreferenceConfig.USER_ID, null));
            user.setName(preferences.getString(PreferenceConfig.USER_NAME, null));
            user.setFullName(preferences.getString(PreferenceConfig.USER_FULL_NAME, null));
            user.setProfilePictureUrl(preferences.getString(PreferenceConfig.USER_PROFILE_PICTURE, null));
            user.setBio(preferences.getString(PreferenceConfig.USER_BIO, null));
            return user;
        }

        return null;
    }

    public static void setUserInfo(Context context, UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        SharedPreferences.Editor editor = getPreferences(context).edit();
        InstaUser user = userInfo.getUser();
        editor.putString(PreferenceConfig.USER_ID, user.getId());
        editor.putString(PreferenceConfig.USER_NAME, user.getName());
        editor.putString(PreferenceConfig.USER_FULL_NAME, user.getFullName());
        editor.putString(PreferenceConfig.USER_PROFILE_PICTURE, user.getProfilePictureUrl());
        editor.putString(PreferenceConfig.USER_BIO, user.getBio());
        editor.putString(PreferenceConfig.ACCESS_TOKEN, userInfo.getAccessToken());
        editor.commit();
    }


    public static void setHostInfo(Context context, HostInfo hostInfo) {
        if (hostInfo == null) {
            return;
        }
        SharedPreferences.Editor editor = getPreferences(context).edit();
        User user = hostInfo.getUser();
        editor.putString(PreferenceConfig.USER_ID, user.getId());
        editor.putString(PreferenceConfig.USER_NAME, user.getName());
        editor.putString(PreferenceConfig.USER_FULL_NAME, user.getFullName());
        editor.putString(PreferenceConfig.USER_PROFILE_PICTURE, user.getProfileImageUrl());
        editor.putString(PreferenceConfig.USER_BIO, user.getBio());
        editor.putString(PreferenceConfig.ACCESS_TOKEN, hostInfo.getAccessToken());
        editor.commit();
    }

    public static void removeAccessToken(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(PreferenceConfig.ACCESS_TOKEN);
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}