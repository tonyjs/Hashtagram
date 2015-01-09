package com.tonyjs.hashtagram.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by tony.park on 14. 11. 12..
 */
public class SlipScrollView extends ScrollView {
    public interface OnScrollCallback {
        public void onScroll(int amountOfScroll);
    }

    public SlipScrollView(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public SlipScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public SlipScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private OnScrollCallback mOnScrollCallback;

    public void setOnScrollCallback(OnScrollCallback onScrollListener) {
        mOnScrollCallback = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int amountScroll = t - oldt;
        if (mOnScrollCallback != null) {
            mOnScrollCallback.onScroll(-amountScroll);
        }
    }
}
