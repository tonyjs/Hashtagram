package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by tonyjs on 15. 1. 29..
 */
public class TransformationManager {
    private static TransformationManager sInstance;

    public static final TransformationManager getInstance() {
        if (sInstance == null) {
            sInstance = new TransformationManager();
        }
        return sInstance;
    }

    private TransformationManager() {}

    private ResizeTransformation mResizeTransformation;
    public ResizeTransformation getResizeTransformation(Context context) {
        if (mResizeTransformation == null) {
            mResizeTransformation = new ResizeTransformation(context);
        }

        return mResizeTransformation;
    }

    private CircleTransformation mCircleTransformation;

    public CircleTransformation getCircleTransformation(Context context) {
        if (mCircleTransformation == null) {
            mCircleTransformation = new CircleTransformation(context);
        }
        return mCircleTransformation;
    }

    private RoundedCornerTransformation mRoundedCornerTransformation;

    public RoundedCornerTransformation getRoundedCornerTransformation(Context context) {
        if (mRoundedCornerTransformation == null) {
            mRoundedCornerTransformation = new RoundedCornerTransformation(context);
        }
        return mRoundedCornerTransformation;
    }

    private CenterCrop mCenterCrop;

    public CenterCrop getCenterCrop(Context context) {
        if (mCenterCrop == null) {
            mCenterCrop = new CenterCrop(context);
        }
        return mCenterCrop;
    }

    public static class ResizeTransformation extends BitmapTransformation {

        public ResizeTransformation(Context context) {
            super(context);
            Log.e("ResizeTransformation", "Constructor");
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, true);
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
            Log.e("RoundedCornerTransformation", "Constructor");
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
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
            Log.e("CircleTransformation", "Constructor");
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
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
}
