package com.tonyjs.hashtagram.io.request.volley.response;

import com.android.volley.VolleyError;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public abstract class SimpleCallback<T> implements Callback<T>{
    @Override
    public void onSuccess(T response) {}

    @Override
    public void onError(VolleyError e) {}
}
