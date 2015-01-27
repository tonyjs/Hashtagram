package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public class ToastManager {
    private static ToastManager sInstance;

    private ToastManager(){}

    public static ToastManager getInstance() {
        if (sInstance == null) {
            sInstance = new ToastManager();
        }
        return sInstance;
    }


    public void show(Context context, Object msg) {
        show(context, msg.toString());
    }

    public void show(Context context, Exception e) {
        show(context, e.toString());
    }

    public void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    private Toast mToast;
    public void show(Context context, String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

}
