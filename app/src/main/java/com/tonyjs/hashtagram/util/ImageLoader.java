package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by tonyjs on 15. 1. 23..
 */
public class ImageLoader {
    public enum TransformationType {
        CENTER_CROP, ROUNDED_CORNER, CIRCLE
    }

    public static final int NONE_RESOURCE_ID = -1;

    private final RequestManager.ImageModelRequest<String> mGlideRequest;

    private final CenterCrop mCenterCrop;
    private final DefaultBitmapTransformation mDefaultTransformation;
    private final RoundedCornerTransformation mRoundedCornerTransformation;
    private final CircleTransformation mCircleTransformation;
    public ImageLoader(Context context) {
        DefaultImageLoader loader = new DefaultImageLoader(context);
        mGlideRequest = Glide.with(context).using(loader);
        mCenterCrop = new CenterCrop(context);
        mDefaultTransformation = new DefaultBitmapTransformation(context);
        mRoundedCornerTransformation = new RoundedCornerTransformation(context);
        mCircleTransformation = new CircleTransformation(context);
    }

    public void load(ImageView imageView, String url) {
        load(imageView, url, true, null, NONE_RESOURCE_ID, NONE_RESOURCE_ID);
    }

    public void load(ImageView imageView, String url, TransformationType type) {
        Transformation transformation = mDefaultTransformation;

        switch (type) {
            case CENTER_CROP:
                transformation = mCenterCrop;
                break;
            case ROUNDED_CORNER:
                transformation = mRoundedCornerTransformation;
                break;
            case CIRCLE:
                transformation = mCircleTransformation;
                break;
        }

        load(imageView, url, true, transformation, NONE_RESOURCE_ID, NONE_RESOURCE_ID);
    }

    public void load(ImageView imageView, String url, boolean animate) {
        load(imageView, url, animate, null, NONE_RESOURCE_ID, NONE_RESOURCE_ID);
    }

    public void load(ImageView imageView, String url,
                     int waitingImageResId, int errorImageResId) {
        load(imageView, url, true, null, waitingImageResId, errorImageResId);
    }

    public void load(ImageView imageView, String url,
                     boolean animate,  Transformation transformation,
                     int waitingImageResId, int errorImageResId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (isGif(url)) {
            Log.d("jsp", "is gif");
            loadGif(imageView, url, animate, transformation, waitingImageResId, errorImageResId);
            return;
        }

        BitmapTypeRequest<String> request = mGlideRequest.load(url).asBitmap();

        if (!animate) {
            request.dontAnimate();
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

    public void loadResizeBitmap(final ImageView imageView, String url, int width, int height) {
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

        load(url, target);
    }

    public void load(String url, BaseTarget target) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        BitmapTypeRequest<String> request = mGlideRequest.load(url).asBitmap();
        request.transform(mDefaultTransformation);
        request.dontAnimate();
        request.into(target);
    }

    public void loadGif(ImageView imageView, String url,
                     boolean animate, Transformation transformation,
                     int waitingImageResId, int errorImageResId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        DrawableTypeRequest<String> request = mGlideRequest.load(url);

        if (!animate) {
            request.dontAnimate();
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
        return format.contains("gif");
    }

    public static class DefaultBitmapTransformation extends BitmapTransformation {

        public DefaultBitmapTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, true);
            toTransform.recycle();
            return resizeBitmap;
        }

        @Override
        public String getId() {
            return "com.tonyjs.DefaultBitmapTransformation";
        }
    }

    public static class RoundedCornerTransformation extends BitmapTransformation {

        private Context mContext;
        public RoundedCornerTransformation(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            int width = toTransform.getWidth();
            int height = toTransform.getHeight();
            Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            int radius = (int) (5 * mContext.getResources().getDisplayMetrics().density);
            Canvas canvas = new Canvas(result);

            RectF rectF = new RectF(0, 0, width, height);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            BitmapShader shader = new BitmapShader(
                    toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return "com.tonyjs.RoundedCornerTransformation";
        }
    }

    public static class CircleTransformation extends BitmapTransformation {

        public CircleTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            int width = toTransform.getWidth();
            int height = toTransform.getHeight();
            Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);

            RectF rectF = new RectF(0, 0, width, height);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            BitmapShader shader = new BitmapShader(
                    toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            canvas.drawOval(rectF, paint);
            return result;
        }

        @Override
        public String getId() {
            return "com.tonyjs.CircleTransformation";
        }
    }

    private static final ModelCache<String, GlideUrl> sUrlCache = new ModelCache<>(150);
    private static class DefaultImageLoader extends BaseGlideUrlLoader<String> {

        public DefaultImageLoader(Context context) {
            super(context, sUrlCache);
        }

        @Override
        protected String getUrl(String model, int width, int height) {
            return model;
        }
    }
}
