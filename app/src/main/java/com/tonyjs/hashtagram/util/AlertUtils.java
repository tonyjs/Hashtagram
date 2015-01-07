package com.tonyjs.hashtagram.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by tony.park on 14. 12. 11..
 */
public class AlertUtils {
    public static void alert(Activity activity, Object msg,
                            DialogInterface.OnClickListener okListener) {
        alert(activity, msg.toString(), okListener, null);
    }

    public static void alert(Activity activity, Object msg) {
        alert(activity, msg.toString(), null, null);
    }

    public static void alert(Activity activity, String msg) {
        alert(activity, msg, null, null);
    }

    public static void alert(Activity activity, String msg,
                             DialogInterface.OnClickListener okListener) {
        alert(activity, msg, okListener, null);
    }

    public static void alert(Activity activity, String msg,
                             DialogInterface.OnClickListener okListener,
                             DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(activity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .create().show();
    }

    public static void notCancellableAlert(Activity activity, String msg,
                                           DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, okListener)
                .create().show();
    }

}
