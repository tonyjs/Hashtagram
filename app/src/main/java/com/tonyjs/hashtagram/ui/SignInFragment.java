package com.tonyjs.hashtagram.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.config.InstaConfig;
import com.tonyjs.hashtagram.ui.view.BasicWebView;

/**
 * Created by orcpark on 2014. 9. 4..
 */
public class SignInFragment extends Fragment {
    public interface SignInCallback{
        public void onSignIn(String code);
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(getActivity());
        CookieManager.getInstance().removeAllCookie();
    }

    private ViewGroup mRootView;
    private BasicWebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_in, container, false);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getActivity().getResources().getString(
                R.string.loading
        ));

//        BasicWebView webView = (BasicWebView) rootView.findViewById(R.id.web_view);
        mWebView = new BasicWebView(getActivity());
        mWebView.clearCache(true);
        SignInCallback callback =
                getActivity() instanceof SignInCallback ?
                        (SignInCallback) getActivity() : null;
        mWebView.setWebViewClient(new SignInWebViewClient(progressDialog, callback));
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

    private static class SignInWebViewClient extends WebViewClient{

        private ProgressDialog mProgressDialog;
        private SignInCallback mCallback;

        public SignInWebViewClient(ProgressDialog progressDialog,
                                   SignInCallback signInCallback) {

            mProgressDialog = progressDialog;
            mCallback  = signInCallback;
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
                    String[] pair = url.split("code=");
                    String accessToken = pair[1];
                    if (mCallback != null) {
                        mCallback.onSignIn(accessToken);
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
    }
}
