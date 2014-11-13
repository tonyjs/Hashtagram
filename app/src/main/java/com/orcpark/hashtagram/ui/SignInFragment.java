package com.orcpark.hashtagram.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.config.InstaConfig;
import com.orcpark.hashtagram.io.OnFinishedListener;
import com.orcpark.hashtagram.ui.view.BasicWebView;

/**
 * Created by orcpark on 2014. 9. 4..
 */
public class SignInFragment extends Fragment{

    private OnFinishedListener mOnFinishedListener;

    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        mOnFinishedListener = onFinishedListener;
    }

    public static SignInFragment newInstance(OnFinishedListener onFinishedListener) {
        SignInFragment fragment = new SignInFragment();
        fragment.setOnFinishedListener(onFinishedListener);
        return fragment;
    }

    private ViewGroup mRootView;
    private BasicWebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_in, container, false);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중입니다. 잠시만 기다려주세요.");

//        BasicWebView webView = (BasicWebView) rootView.findViewById(R.id.web_view);
        mWebView = new BasicWebView(getActivity());
        mWebView.setWebViewClient(new InstaWebViewClient(progressDialog, mOnFinishedListener));

        mRootView.addView(mWebView);

        mWebView.loadUrl(InstaConfig.INSTA_AUTHORIZATION_URL);
//        webView.loadUrl("http://m.naver.com");

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {
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
                if (url.startsWith(InstaConfig.INSTA_REDIRECT_URI)) {
                    String[] pair = url.split("access_token=");
                    String accessToken = pair[1];
                    mOnFinishedListener.onFinished(accessToken);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
    }
}
