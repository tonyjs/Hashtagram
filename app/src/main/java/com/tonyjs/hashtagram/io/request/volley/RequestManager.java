package com.tonyjs.hashtagram.io.request.volley;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public class RequestManager {
    private static RequestManager sInstance;

    private RequestManager() {}

    public static RequestManager getInstance() {
        if(sInstance == null) {
            sInstance = new RequestManager();
        }
        return sInstance;
    }

    private RequestQueue mRequestQueue;
    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    private ImageLoader mImageLoader;
    public ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getRequestQueue(context), WEAK_IMAGE_CACHE);
        }
        return mImageLoader;
    }

    public static WeakImageCache WEAK_IMAGE_CACHE = new WeakImageCache();
    public static class WeakImageCache implements ImageLoader.ImageCache{
        private final static int MAX_SIZE = 4096;
        private ConcurrentHashMap<String, WeakReference<Bitmap>> map = new ConcurrentHashMap<String, WeakReference<Bitmap>>();

        @Override
        public void putBitmap(String k, Bitmap v){
            if(map.size() > MAX_SIZE){
                map.clear();
            }
            map.put(k, new WeakReference<>(v));
        }

        @Override
        public Bitmap getBitmap(String key){
            WeakReference<Bitmap> ref = map.get(key);

            if(ref == null){
                return null;
            }

            return ref.get();
        }
    }
}
