package com.tonyjs.hashtagram.io.request.volley;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public abstract class BasicRequest<T> extends Request<T> {
    public static final String TAG = BasicRequest.class.getSimpleName();

    private Callback mCallback;
    public BasicRequest(int method, String url, Callback callback) {
        super(method, url, null);
        mCallback = callback;
        setRetryPolicy(new RequestRetryPolicy());
    }

    private String mTitle;
    public void setTitle(String title) {
        mTitle = title;
    }

    private View mProgressView;
    public void setProgressView(View view) {
        mProgressView = view;
    }

    private Dialog mDialog;
    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    private HashMap<String, String> mParams;
    public void setParams(HashMap<String, String> params) {
        mParams = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    private Map<String, String> mHeaders = Collections.emptyMap();
    public void setHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders = headers;
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public Request<T> setRequestQueue(RequestQueue requestQueue) {
        super.setRequestQueue(requestQueue);
        if (!TextUtils.isEmpty(mTitle)) {
            Log.d(TAG, "RequestStart - " + mTitle);
        }
        if (mDialog != null) {
            mDialog.show();
        }
        if (mProgressView != null) {
            mProgressView.setVisibility(View.VISIBLE);
        }
        return this;
    }

    @Override
    protected void deliverResponse(T response) {
        if (!TextUtils.isEmpty(mTitle)) {
            Log.d(TAG, "ResponseDelivered - " + mTitle);
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
        }
        if (mCallback != null) {
            mCallback.onSuccess(response);
        }
    }


    @Override
    public void deliverError(VolleyError error) {
        Log.e(TAG, getParseError(error, mTitle));
        if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (mCallback != null) {
            mCallback.onError(error);
        }
    }

    public static String getParseError(VolleyError error, String title) {
        if (error == null) {
            return "Error is null";
        }
        NetworkResponse errorResponse = error.networkResponse;
        if (errorResponse == null) {
            return error.toString();
        }

        String tag = "";
        if (!TextUtils.isEmpty(title)) {
            tag = "(" + title + ")";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("------------ Volley Error" + tag + " ------------\n");
        sb.append("httpStatusCode : " + errorResponse.statusCode + "\n");
        try {
            String errorBody = new String(errorResponse.data, "UTF-8");
            sb.append("body : " + errorBody + "\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("------------ Volley Error" + tag + " ------------");
        return sb.toString();
    }

    private class RequestRetryPolicy extends DefaultRetryPolicy {
        public static final int DEFAULT_TIMEOUT = 3000; // 3 second
        public static final int MAX_RETRIES = 1;
        public static final float BACK_OFF = 1f;

        public RequestRetryPolicy(){
            super(DEFAULT_TIMEOUT, MAX_RETRIES, BACK_OFF);
        }
    }
}
