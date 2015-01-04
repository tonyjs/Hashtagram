package com.orcpark.hashtagram.util;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orcpark.hashtagram.ui.widget.RoundedDrawable;

/**
 * Created by tony.park on 14. 11. 5..
 */
public class ImageLoader {

    public static final int NONE = -1;

    public static void loadWithCallback(
            Context context, String url, SimpleTarget target) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(target);
    }

    public static void loadCircleDrawable(
            Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .transform(new CircleTransform(context))
                .into(imageView);
    }

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

    public static class CircleTransform extends BitmapTransformation {

        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            RoundedDrawable roundedDrawable = new RoundedDrawable(toTransform);
            return roundedDrawable.getBitmap();
        }

        @Override
        public String getId() {
            return "com.orcpark.hashtagram.circletransform";
        }
    }

}
