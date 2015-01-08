package com.tonyjs.hashtagram.io.request.retrofit;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.tonyjs.hashtagram.util.PrefUtils;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by tonyjs on 15. 1. 8..
 */
public class RequestTask
        extends AsyncTask<RequestTask.Type, Void, Response> implements ErrorHandler{

    public enum Type {
        GET_NEWS_FEED, GET_HOST_INFO
    }

    public interface Callback<T extends Response>{
        public void callback(T response);
    }

    private Context mContext;
    private RequestInterface mRequester;
    public RequestTask(Context context) {
        mContext = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(RequestInterface.END_POINT)
                .setErrorHandler(this)
                .build();
        mRequester = restAdapter.create(RequestInterface.class);
    }

    private Callback mCallback;

    public RequestTask setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    private View mProgress;

    public RequestTask setProgressView(View progressView) {
        mProgress = progressView;
        return this;
    }

    private Dialog mDialog;
    public RequestTask setDialog(Dialog dialog) {
        mDialog = dialog;
        return this;
    }

    public RequestTask run(Type type) {
        executeOnExecutor(THREAD_POOL_EXECUTOR, type);
        return this;
    }

    public RequestTask run(Type type, Callback callback) {
        setCallback(callback);
        executeOnExecutor(THREAD_POOL_EXECUTOR, type);
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgress != null) {
            mProgress.setVisibility(View.VISIBLE);
        }

        if (mDialog != null) {
            mDialog.show();
        }
    }

    private String mErrorMessage;
    @Override
    protected Response doInBackground(Type... types) {
        if (mContext == null || isCancelled()) {
            mErrorMessage = "isCancelled";
            if (mContext == null) {
                mErrorMessage = "Context is null";
            }
            return null;
        }

        Type type = types[0];
        if (type == null) {
            mErrorMessage = "Type has not set";
            return null;
        }

        switch (type) {
            case GET_NEWS_FEED:
                String accessToken = PrefUtils.getAccessToken(mContext);
                if (TextUtils.isEmpty(accessToken)) {
                    mErrorMessage = "AccessToken is null";
                    return null;
                }
                return mRequester.getNewsFeed(accessToken);
            default:
                mErrorMessage = "known";
                return null;
        }
    }

    @Override
    protected void onPostExecute(Response t) {
        if (mCallback != null) {
            mCallback.callback(t);
        }

        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
        }
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    private RetrofitError mError;

    public RetrofitError getError() {
        return mError;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        return mError = cause;
    }
}
