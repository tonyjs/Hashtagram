package com.orcpark.hashtagram.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class PullCatchListView extends ListView {

    public static final int MIN_TOUCH_AMOUNT = 10;

    public interface OnPullListener{
        public void onPulled();
    }

    public PullCatchListView(Context context) {
        super(context);
        init();
    }

    public PullCatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullCatchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    int mMinTouchAmount;
    private void init() {
        mMinTouchAmount = (int) (MIN_TOUCH_AMOUNT * getContext().getResources().getDisplayMetrics().density);
    }

    private OnPullListener mOnPullListener;

    public void setOnPullListener(OnPullListener onPullListener) {
        mOnPullListener = onPullListener;
    }

    private boolean mFirstOnTop = false;
    private boolean mScrolled = false;

    private float mFirstX;
    private float mFirstY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isChildViewOnTop()) {
                    mFirstOnTop = true;
                }
                mFirstX = ev.getX();
                mFirstY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                float y = ev.getY();

                int distanceY = (int) (y - mFirstY) ;
                int absDistanceY = Math.abs(distanceY);
                int absDistanceX = Math.abs((int) (x - mFirstX));

                if (distanceY > 0
                        && absDistanceY > absDistanceX
                        && absDistanceY > mMinTouchAmount) {
                    mScrolled = true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mScrolled && getChildCount() <= 0) {
                    if (mOnPullListener != null) {
                        mOnPullListener.onPulled();
                    }
                    mScrolled = false;
                    return super.onTouchEvent(ev);
                }

                if (mFirstOnTop && mScrolled && isChildViewOnTop()) {
                    if (mOnPullListener != null) {
                        mOnPullListener.onPulled();
                    }
                }
                mFirstOnTop = false;
                mScrolled = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isChildViewOnTop() {
        if (getChildCount() > 0) {
            View childView = getChildAt(0);
            return (getTop() + getPaddingTop()) == childView.getTop();
        }
        return false;
    }

}
