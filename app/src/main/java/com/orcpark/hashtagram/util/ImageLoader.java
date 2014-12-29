package com.orcpark.hashtagram.util;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

/**
 * Created by tony.park on 14. 11. 5..
 */
public class ImageLoader {

    public static final int NONE = -1;

    public static void load(Context context, String url, ImageView imageView) {
        load(context, url, imageView, false, NONE, NONE);
    }

    public static void load(Context context, String url, ImageView imageView, boolean animate) {
        load(context, url, imageView, animate, NONE, NONE);
    }

    public static void load(Context context, String url, ImageView imageView,
                            boolean animate, int waitingImageResId) {
        load(context, url, imageView, animate, waitingImageResId, NONE);
    }

    public static void load(Context context, String url, ImageView imageView,
                            boolean animate, int waitingImageResId, int errorImageResId) {
        DrawableTypeRequest<String> request = getRequest(context, url);

        request.dontTransform();
        if (!animate) {
            request.dontAnimate();
        }

        if (waitingImageResId != NONE) {
            request.placeholder(waitingImageResId);
        }

        if (errorImageResId != NONE) {
            request.error(errorImageResId);
        }

        request.into(imageView);
    }

    public static DrawableTypeRequest<String> getRequest(Context context, String url) {
        return Glide.with(context).load(url);
    }
}
