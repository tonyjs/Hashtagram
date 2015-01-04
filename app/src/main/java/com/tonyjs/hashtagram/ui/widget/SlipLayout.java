package com.tonyjs.hashtagram.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by im026 on 2014. 9. 17..
 */
public class SlipLayout extends FrameLayout
        implements AbsListView.OnScrollListener, SlipScrollView.OnScrollListener{
    public static final int DEFAULT_SPARE = 10;
    public SlipLayout(Context context) {
        super(context);
        init();
    }

    public SlipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlipLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    int mSpare;
    private void init() {
        mSpare = (int) (DEFAULT_SPARE * getContext().getResources().getDisplayMetrics().density);
    }

    protected SlipScrollView mScrollView;

    public void setScrollView(SlipScrollView scrollView) {
        mScrollView = scrollView;
        mScrollView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(int amountOfScroll) {
        calculateAndSlipLayout(amountOfScroll);
    }

    private RecyclerView mRecyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setOnScrollListener(new RecyclerScrollListener());
    }

    protected ListView mListView;

    public void setListView(ListView listView) {
        if (listView == null) {
            return;
        }
        mListView = listView;
        mListView.setOnScrollListener(this);
    }

    public ListView getListView(){
        return mListView;
    }

    protected View mTargetView;

    public void setTargetView(View view) {
        mTargetView = view;
    }

    public View getTargetView() {
        return mTargetView;
    }

    private int mTargetViewHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTargetView != null) {
            mTargetViewHeight = mTargetView.getMeasuredHeight();
        }
    }

    protected int mTopMargin = 0;

    protected int mLastScrollY = 0;

    protected void deliverScrollY(int scrollY) {
        if (mTargetView == null) {
            return;
        }
        if (mLastScrollY == 0) {
            mLastScrollY = scrollY;
        }

        int amountOfScrollY = mLastScrollY - scrollY;

        mLastScrollY = scrollY;

        calculateAndSlipLayout(amountOfScrollY);
    }

    protected void calculateAndSlipLayout(int amountOfScrollY) {
        if (amountOfScrollY == 0) {
            return;
        }

        if (Math.abs(amountOfScrollY) >= mTargetViewHeight) {
            if (amountOfScrollY > 0) {
                mTopMargin = 0;
            } else {
                mTopMargin = -mTargetViewHeight;
            }
        } else {
            mTopMargin = mTopMargin + amountOfScrollY;
            if (Math.abs(mTopMargin) >= mTargetViewHeight) {
                if (mTopMargin > 0) {
                    mTopMargin = 0;
                } else {
                    mTopMargin = -mTargetViewHeight;
                }
            } else {
                if (mTopMargin > 0) {
                    mTopMargin = 0;
                }
            }
        }

        if (mTargetView != null) {
            MarginLayoutParams params = (MarginLayoutParams) mTargetView.getLayoutParams();
            params.topMargin = mTopMargin;
            mTargetView.setLayoutParams(params);
        }
    }

    public void showTargetViewForcibly() {
        if (mTargetView != null) {
            MarginLayoutParams params = (MarginLayoutParams) mTargetView.getLayoutParams();
            params.topMargin = 0;
            mTargetView.setLayoutParams(params);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

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

    public class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            calculateAndSlipLayout(-dy);
        }
    }
}
