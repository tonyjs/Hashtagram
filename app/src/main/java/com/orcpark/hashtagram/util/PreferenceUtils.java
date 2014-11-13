package com.orcpark.hashtagram.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import com.orcpark.hashtagram.config.PreferenceConfig;

import java.util.Set;

/**
 * Created by orcpark on 2014. 9. 7..
 */
public class PreferenceUtils {

    public static boolean hasInstalled(Context context) {
        return getPreferences(context).getBoolean(PreferenceConfig.HAS_INSTALLED, false);
    }

    public static void setHasInstalled(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(PreferenceConfig.HAS_INSTALLED, true);
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

    public static void setHashSet(Context context, Set hashSet) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putStringSet(PreferenceConfig.HASH_SET, hashSet);
        editor.commit();
    }

    public static Set<String> getHashSet(Context context) {
        return getPreferences(context).getStringSet(PreferenceConfig.HASH_SET, null);
    }

    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}