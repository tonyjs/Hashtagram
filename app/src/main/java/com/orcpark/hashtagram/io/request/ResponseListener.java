package com.orcpark.hashtagram.io.request;

import com.android.volley.VolleyError;

/**
 * Created by tony.park on 14. 11. 4..
 */
public interface ResponseListener<T> {

    public void onResponse(T response);

    public void onError(VolleyError error);

}