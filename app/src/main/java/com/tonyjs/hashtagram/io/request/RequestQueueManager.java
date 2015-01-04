package com.tonyjs.hashtagram.io.request;

import android.content.Context;
import android.graphics.Bitmap;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tony.park on 14. 11. 4..
 */
public class RequestQueueManager {
    public static WeakImageCache defaultImageCache = new WeakImageCache();
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    private static RequestQueueManager sInstance;

    private RequestQueueManager(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, defaultImageCache);
    }

    public static RequestQueueManager getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new RequestQueueManager(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static class WeakImageCache implements ImageLoader.ImageCache{
        private final static int MAX_SIZE = 4096;
        private ConcurrentHashMap<String, WeakReference<Bitmap>> map = new ConcurrentHashMap<String, WeakReference<Bitmap>>();

        @Override
        public void putBitmap(String k, Bitmap v){
            if(map.size() > MAX_SIZE){
                map.clear();
            }
            map.put(k, new WeakReference<Bitmap>(v));
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
