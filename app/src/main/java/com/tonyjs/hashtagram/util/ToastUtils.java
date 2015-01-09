package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tony.park on 14. 12. 11..
 */
public class ToastUtils {
    public static void show(Context context, Object msg) {
        show(context, msg.toString());
    }

    public static void show(Context context, Exception e) {
        show(context, e.toString());
    }

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int length) {
        Toast.makeText(context, msg, length).show();
    }
}
