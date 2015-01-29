package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by tonyjs on 15. 1. 23..
 */
public class ImageLoader {
    public enum TransformationType {
        CENTER_CROP, ROUNDED_CORNER, CIRCLE
    }

    public static final int NONE_RESOURCE_ID = -1;

    public void loadFile(Context context, File file, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (file == null || !file.exists()) {
            return;
        }

        DrawableTypeRequest<File> request = Glide.with(context).load(file);
        String fileName = file.getName();
        if (!isGif(fileName)) {
            request.asBitmap();
        }
        request.into(imageView);
    }

    public void loadFile(Context context, File file, BaseTarget baseTarget) {
        if (context == null) {
            return;
        }

        if (file == null || !file.exists()) {
            return;
        }

        DrawableTypeRequest<File> request = Glide.with(context).load(file);
        String fileName = file.getName();
        if (!isGif(fileName)) {
            request.asBitmap();
        }
        request.into(baseTarget);
    }

    public static void load(Context context, ImageView imageView, String url) {
        load(context, imageView, url, true, null, NONE_RESOURCE_ID, NONE_RESOURCE_ID);
    }

    public static void load(Context context, ImageView imageView, String url, TransformationType type) {
        Transformation transformation = null;

        TransformationManager transformationManager = TransformationManager.getInstance();
        switch (type) {
            case CENTER_CROP:
                transformation = transformationManager.getCenterCrop(context);
                break;
            case ROUNDED_CORNER:
                transformation = transformationManager.getRoundedCornerTransformation(context);
                break;
            case CIRCLE:
                transformation = transformationManager.getCircleTransformation(context);
                break;
        }

        load(context, imageView, url, true, transformation, NONE_RESOURCE_ID, NONE_RESOURCE_ID);
    }

    public static void load(Context context, ImageView imageView, String url, int waitingImageResId) {
        load(context, imageView, url, true, null, waitingImageResId, NONE_RESOURCE_ID);
    }

    public static void load(Context context, ImageView imageView, String url, boolean animate) {
        load(context, imageView, url, animate, null, NONE_RESOURCE_ID, NONE_RESOURCE_ID);
    }

    public static void load(Context context, ImageView imageView, String url, boolean animate, int waitingImageResId) {
        load(context, imageView, url, animate, null, waitingImageResId, NONE_RESOURCE_ID);
    }

    public static void load(Context context, ImageView imageView, String url,
                     int waitingImageResId, int errorImageResId) {
        load(context, imageView, url, true, null, waitingImageResId, errorImageResId);
    }

    public static void load(Context context, ImageView imageView, String url,
                     boolean animate,  Transformation transformation,
                     int waitingImageResId, int errorImageResId) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (isGif(url)) {
            loadGif(context, imageView, url, animate,
                    transformation, waitingImageResId, errorImageResId);
            return;
        }

        BitmapTypeRequest<String> request = Glide.with(context).load(url).asBitmap();

        if (transformation != null) {
            request.transform(transformation);
        } else {
            request.dontTransform();
        }

        if (!animate) {
            request.dontAnimate();
        } else {
            request.animate(sAnimator);
        }

        if (waitingImageResId != NONE_RESOURCE_ID) {
            request.placeholder(waitingImageResId);
        }
        if (errorImageResId != NONE_RESOURCE_ID) {
            request.error(errorImageResId);
        }

        request.into(imageView);
    }

    public static void loadResizeBitmap(Context context, final ImageView imageView, String url,
                                 int width, int height) {
        SimpleTarget<Bitmap> target =
            new SimpleTarget<Bitmap>(width, height){
                @Override
                public void onResourceReady(Bitmap resource,
                                            GlideAnimation<? super Bitmap> glideAnimation) {
                    Log.e("jsp", "width = " + resource.getWidth()
                            + " & height = " + resource.getHeight());
                    imageView.setImageBitmap(resource);
                }
            };

        load(context, url, target);
    }

    public static void load(Context context, String url, BaseTarget target) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }

        BitmapTypeRequest<String> request = Glide.with(context).load(url).asBitmap();
        request.transform(TransformationManager.getInstance().getResizeTransformation(context));
        request.dontAnimate();
        request.into(target);
    }

    public static void loadGif(Context context, ImageView imageView, String url,
                     boolean animate, Transformation transformation,
                     int waitingImageResId, int errorImageResId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        DrawableTypeRequest<String> request = Glide.with(context).load(url);

        if (!animate) {
            request.dontAnimate();
        } else {
            request.animate(sAnimator);
        }

        if (transformation != null) {
            request.transform(transformation);
        } else {
            request.dontTransform();
        }

        if (waitingImageResId != NONE_RESOURCE_ID) {
            request.placeholder(waitingImageResId);
        }
        if (errorImageResId != NONE_RESOURCE_ID) {
            request.error(errorImageResId);
        }

        request.into(imageView);
    }

    public static boolean isGif(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        String[] split = url.split("\\.");
        int max = split.length;
        if (max <= 0) {
            return false;
        }
        String format = split[max - 1];
        return format.toLowerCase().contains("gif");
    }

    private static final DefaultAnimator sAnimator = new DefaultAnimator();
    public static final class DefaultAnimator implements ViewPropertyAnimation.Animator {

        @Override
        public void animate(View view) {
            view.setAlpha(0.0f);
            view.animate()
                    .alpha(1.0f)
                    .setDuration(200);
        }
    }
}
