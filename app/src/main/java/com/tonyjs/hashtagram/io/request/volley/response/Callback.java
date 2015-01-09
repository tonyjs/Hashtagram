package com.tonyjs.hashtagram.io.request.volley.response;

import com.android.volley.VolleyError;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public interface Callback<T> {
    public void onSuccess(T response);

    public void onError(VolleyError e);
}
