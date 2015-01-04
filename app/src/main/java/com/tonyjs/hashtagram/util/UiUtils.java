package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by JunSeon Park on 14. 3. 12.
 */
public class UiUtils {
    public static int getDPFromPixelSize(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float result = displayMetrics.density * px;
        return (int) result;
    }

}