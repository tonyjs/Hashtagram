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

    public static void removeAccessToken(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(PreferenceConfig.ACCESS_TOKEN);
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}