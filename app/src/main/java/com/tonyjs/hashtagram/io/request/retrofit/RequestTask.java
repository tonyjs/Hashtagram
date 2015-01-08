package com.tonyjs.hashtagram.io.request.retrofit;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.tonyjs.hashtagram.util.PrefUtils;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Query;

/**
 * Created by tonyjs on 15. 1. 8..
 */
public class RequestTask extends AsyncTask<RequestTask.Type, Void, Response>
                implements ErrorHandler{

    public enum Type {
        GET_NEWS_FEED
    }

    public interface Callback<T extends Response>{
        public void callback(T response);
    }

    private Context mContext;
    private Requester mRequester;
    public RequestTask(Context context) {
        mContext = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Requester.END_POINT)
                .setErrorHandler(this)
                .build();
        mRequester = restAdapter.create(Requester.class);
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgress != null) {
            mProgress.setVisibility(View.VISIBLE);
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

        String accessToken = PrefUtils.getAccessToken(mContext);
        if (TextUtils.isEmpty(accessToken)) {
            mErrorMessage = "AccessToken is null";
            return null;
        }

        Type type = types[0];
        if (type == null) {
            mErrorMessage = "Type has not set";
            return null;
        }

        switch (type) {
            case GET_NEWS_FEED:
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

        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
        }
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        return cause;
    }
}
