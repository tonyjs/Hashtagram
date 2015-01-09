package com.tonyjs.hashtagram.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by tony.js on 2014. 9. 17..
 */
public class SlipLayoutController
        implements AbsListView.OnScrollListener, SlipScrollView.OnScrollCallback {

    public static final int DIRECTION_TO_UP = 0;
    public static final int DIRECTION_TO_BOTTOM = 1;

    public SlipLayoutController() {

    }

//    private Context mContext;
//    public SlipLayoutController(Context context) {
//        mContext = context;
//    }

    private int mDirection = DIRECTION_TO_BOTTOM;

    public void setDirection(int direction) {
        mDirection = direction;
    }

    protected SlipScrollView mScrollView;

    public void setScrollView(SlipScrollView scrollView) {
        mScrollView = scrollView;
        mScrollView.setOnScrollCallback(this);
    }

    @Override
    public void onScroll(int amountOfScroll) {
        calculateAndSlipLayout(amountOfScroll);
    }

    protected ListView mListView;

    public void setListView(ListView listView) {
        mListView = listView;
        mListView.setOnScrollListener(this);
    }

    private RecyclerView mRecyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setOnScrollListener(new RecyclerScrollListener());
    }

    protected View mTargetView;

    public void setTargetView(View view) {
        mTargetView = view;
    }

    public View getTargetView() {
        return mTargetView;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getChildCount() <= 0) {
            return;
        }

        View firstView = view.getChildAt(0);
        if (firstView == null) {
            return;
        }

        int scroll = -firstView.getTop() + firstVisibleItem * firstView.getHeight();

        deliverScrollY(scroll);
    }

    protected int mLastScrollY = 0;
    protected void deliverScrollY(int scrollY) {
        if (mTargetView == null) {
            return;
        }
        if (mLastScrollY == 0) {
            mLastScrollY = scrollY;
        }

        int amountOfScrollY = scrollY - mLastScrollY;

        mLastScrollY = scrollY;

        calculateAndSlipLayout(-amountOfScrollY);
    }

    protected int mMargin = 0;
    protected void calculateAndSlipLayout(int amountOfScrollY) {
        if (amountOfScrollY == 0 || mTargetView == null) {
            return;
        }

        int slipDistance = mTargetView.getHeight();

        if (Math.abs(amountOfScrollY) >= slipDistance) {
            if (amountOfScrollY > 0) {
                mMargin = 0;
            } else {
                mMargin = -slipDistance;
            }
        } else {
            mMargin = mMargin + amountOfScrollY;
            if (Math.abs(mMargin) >= slipDistance) {
                if (mMargin > 0) {
                    mMargin = 0;
                } else {
                    mMargin = -slipDistance;
                }
            } else {
                if (mMargin > 0) {
                    mMargin = 0;
                }
            }
        }

        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) mTargetView.getLayoutParams();
        if (mDirection == DIRECTION_TO_BOTTOM) {
            params.bottomMargin = mMargin;
        } else {
            params.topMargin = mMargin;
        }

        mTargetView.setLayoutParams(params);
    }

    public void showTargetView() {
        if (mTargetView == null) {
            return;
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mTargetView.getLayoutParams();

        if (mDirection == DIRECTION_TO_BOTTOM) {
            lp.bottomMargin = 0;
        } else {
            lp.topMargin = 0;
        }

        mTargetView.setLayoutParams(lp);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

//    public int getPixelFromDp(float dp) {
//        return (int) (mContext.getResources().getDisplayMetrics().density * dp);
//    }

    private class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            calculateAndSlipLayout(-dy);
        }
    }
}
