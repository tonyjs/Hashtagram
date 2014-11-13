package com.orcpark.hashtagram.ui.view;

import android.content.Context;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by JunSeon Park on 14. 1. 13.
 */
public class BasicWebView extends WebView {

    public BasicWebView(Context context) {
        super(context);
        init();
    }

    public BasicWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BasicWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        if(!isInEditMode()){
            Log.e("jsp", "!!!!!!!!!!!!!!!!!!! init !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            setVerticalScrollbarOverlay(true);
            getSettings().setDefaultTextEncodingName("UTF-8");
            getSettings().setJavaScriptEnabled(true);
            getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            setWebChromeClient(new WebChromeClient());
            setWebViewClient(new GeekWebViewClient());
        }
    }

    private View btnMoveToTop;

    public void setBtnMoveToTop(View btnMoveToTop) {
        this.btnMoveToTop = btnMoveToTop;
        btnMoveToTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollTo(0, 0);
            }
        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(t > getTop() + 4){
            if(btnMoveToTop != null){
                btnMoveToTop.setVisibility(View.VISIBLE);
            }
        }else{
            if(btnMoveToTop != null){
                btnMoveToTop.setVisibility(View.GONE);
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private class GeekWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("jsp", "url = " + url);
            if (url.startsWith("http")) {
                view.loadUrl(url);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

}
