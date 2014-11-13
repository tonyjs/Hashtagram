package com.orcpark.hashtagram.io.request;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tony.park on 14. 11. 4..
 */
public class RequestQueueManager {
    private RequestQueue mRequestQueue;

    private static RequestQueueManager sInstance;

    private RequestQueueManager(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
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
}
