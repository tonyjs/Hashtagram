package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tony.park on 14. 12. 11..
 */
public class ToastUtils {
    public static void toast(Context context, Object msg) {
        toast(context, msg.toString());
    }

    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, Exception e) {
        toast(context, e.toString());
    }

    public static void toast(Context context, String msg, int length) {
        Toast.makeText(context, msg, length).show();
    }
}
