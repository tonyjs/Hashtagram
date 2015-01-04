package com.orcpark.hashtagram.io.request;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import com.android.volley.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony.park on 14. 11. 4..
 */
public abstract class Requester<T> extends Request<T> {
    public static final String CHARSET = "UTF-8";

    private ResponseListener mListener;

    public Requester(int method, String url, ResponseListener listener) {
        super(method, url, null);
        setRetryPolicy(new RequestRetryPolicy());
        mListener = listener;
    }

    private View mProgressView;

    public void setProgressView(View view) {
        mProgressView = view;
    }

    private Dialog mDialog;

    public void setProgressDialog(Dialog dialog) {
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
    public void setRequestQueue(RequestQueue requestQueue) {
        super.setRequestQueue(requestQueue);
        if (mProgressView != null) {
            mProgressView.setVisibility(View.VISIBLE);
        }
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }

        if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
        }

        if (mDialog != null) {
            mDialog.cancel();
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
        }

        if (mDialog != null) {
            mDialog.cancel();
        }

        if (mListener != null) {
            mListener.onError(error);
        }
    }

    private class RequestRetryPolicy extends DefaultRetryPolicy {
        public static final int DEFAULT_TIMEOUT = 8000;
        public static final int MAX_RETRIES = 1;
        public static final float BACK_OFF = 1f;

        public RequestRetryPolicy(){
            super(DEFAULT_TIMEOUT, MAX_RETRIES, BACK_OFF);
        }
    }

}
