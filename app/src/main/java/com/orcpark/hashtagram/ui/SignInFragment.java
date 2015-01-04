package com.orcpark.hashtagram.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.config.InstaConfig;
import com.orcpark.hashtagram.io.OnFinishedListener;
import com.orcpark.hashtagram.ui.view.BasicWebView;

/**
 * Created by orcpark on 2014. 9. 4..
 */
public class SignInFragment extends Fragment {

    private OnFinishedListener mOnFinishedListener;

    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        mOnFinishedListener = onFinishedListener;
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof OnFinishedListener) {
            setOnFinishedListener((OnFinishedListener) getActivity());
        }
        CookieSyncManager.createInstance(getActivity());
        CookieManager.getInstance().removeAllCookie();
    }

    private ViewGroup mRootView;
    private BasicWebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_in, container, false);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getActivity().getResources().getString(
                R.string.loading
        ));

//        BasicWebView webView = (BasicWebView) rootView.findViewById(R.id.web_view);
        mWebView = new BasicWebView(getActivity());
        mWebView.clearCache(true);
        mWebView.setWebViewClient(new InstaWebViewClient(progressDialog, mOnFinishedListener));

        mRootView.addView(mWebView);

        mWebView.loadUrl(InstaConfig.INSTA_AUTHORIZATION_URL);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.removeAllViews();
        }
        if (mRootView != null) {
            mRootView.removeAllViews();
        }
        super.onDestroyView();
    }

    private static class InstaWebViewClient extends WebViewClient{

        private ProgressDialog mProgressDialog;
        private OnFinishedListener mOnFinishedListener;

        public InstaWebViewClient(ProgressDialog progressDialog,
                                  OnFinishedListener onFinishedListener) {

            mProgressDialog = progressDialog;
            mOnFinishedListener  = onFinishedListener;
        }

        @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.e("jsp", url);
                if (url.startsWith(InstaConfig.INSTA_REDIRECT_URI)) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
//                    String[] pair = url.split("access_token=");
                    String[] pair = url.split("code=");
                    String accessToken = pair[1];
                    if (mOnFinishedListener != null) {
                        mOnFinishedListener.onFinished(accessToken);
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
    }
}
