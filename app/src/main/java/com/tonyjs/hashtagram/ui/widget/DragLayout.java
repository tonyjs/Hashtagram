package com.tonyjs.hashtagram.ui.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JunSeon Park on 2014-04-24.
 */
public class DragLayout extends ViewGroup {
    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private ViewDragHelper mDragHelper;

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallBack());
    }

    private View mDragTargetView;
    private View mContentView;
    private View mMetaphor;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDragTargetView = getChildAt(0);

        mContentView = getChildAt(1);
    }

    public void setMetaphor(View metaphor) {
        mMetaphor = metaphor;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private float mFirstX;
    private float mFirstY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isClickableChildren(mDragTargetView)) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        if ((action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        final float x = ev.getX();
        final float y = ev.getY();

        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = x;
                mFirstY = y;
                interceptTap = mDragHelper.isViewUnder(mDragTargetView, (int) x, (int) y);
                break;
            case MotionEvent.ACTION_MOVE:
                final float distanceX = Math.abs(x - mFirstX);
                final float distanceY = Math.abs(y - mFirstY);
                final int slop = mDragHelper.getTouchSlop();

                if (distanceY > slop && distanceX > distanceY || !mDragHelper.isViewUnder(mDragTargetView, (int) x, (int) y)) {
                    mDragHelper.cancel();
                    return false;
                }
                break;
        }

        return mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    private boolean isClickableChildren(View view) {
        if (view instanceof ViewGroup) {
            return isClickableChildren((ViewGroup) view);
        }

        if (view.isClickable() || view.isFocusableInTouchMode() || view.isFocusable() || view.isFocused()) {
//            Log.e("jsp", "return true");
            return true;
        }

        return false;
    }

    private boolean isClickableChildren(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    boolean clickableChildren = isClickableChildren((ViewGroup) view);
                    if (clickableChildren) {
                        return true;
                    }
                }

                if (view.isClickable() || view.isFocusableInTouchMode() || view.isFocusable() || view.isFocused()) {
//                    Log.e("jsp", "return true");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        boolean isPoppyViewUnder = mDragHelper.isViewUnder(mDragTargetView, (int) x, (int) y);

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = x;
                mFirstY = y;
                break;
            case MotionEvent.ACTION_UP:
                final float distanceX = x - mFirstX;
                final float distanceY = y - mFirstY;

                final int slop = mDragHelper.getTouchSlop();

                if (distanceX * distanceX + distanceY * distanceY < slop * slop
                        && isPoppyViewUnder) {
                    if (mDragOffset == 0) {
                        smoothSlideTo(1f);
                    } else {
                        smoothSlideTo(0f);
                    }
                    return true;
                }

                if (Math.abs(distanceY) > distanceX) {
                    if (distanceY > 0) {
                        smoothSlideTo(1f);
                    } else {
                        smoothSlideTo(0f);
                    }
                    break;
                }
        }

        return isPoppyViewUnder;
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth()
                && screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    public boolean smoothSlideTo(float slideOffset) {
        if (mMetaphor != null) {
            if (slideOffset == 0) {
                mMetaphor.setSelected(false);
            } else {
                mMetaphor.setSelected(true);
            }
        }

        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);

        if (mDragHelper.smoothSlideViewTo(mDragTargetView, mDragTargetView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    public boolean isOnTop() {
        return !mMetaphor.isSelected();
    }

    public void collapsePane() {
        smoothSlideTo(1f);
    }

    private int mDragViewHeight;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        mDragViewHeight = mDragTargetView.getMeasuredHeight();
//        Log.e("jsp", "dragViewHeight - " + mDragViewHeight);

        int contentViewHeight = height - paddingTop - paddingBottom - mDragViewHeight;
        LayoutParams lp = mContentView.getLayoutParams();
        lp.height = contentViewHeight;

        int contentViewWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                paddingLeft + paddingRight, mContentView.getMeasuredWidth());
        int contentViewHeightSpec =
                MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY);
        mContentView.measure(contentViewWidthSpec, contentViewHeightSpec);
//
        setMeasuredDimension(width, height);
    }

    private void measureContentViewChildrenIfNeed(int parentWidthSpec, int parentHeightSpec) {
        if (mContentView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) mContentView;
            int childCount = viewGroup.getChildCount();
            if (childCount <= 0) {
                return;
            }

            int paddingLeft = mContentView.getPaddingLeft();
            int paddingRight = mContentView.getPaddingRight();
            int paddingTop = mContentView.getPaddingTop();
            int paddingBottom = mContentView.getPaddingBottom();

            for (int i = 0; i < childCount; i++) {
                View v = viewGroup.getChildAt(i);

                MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();

                int widthSpec = getChildMeasureSpec(parentWidthSpec,
                        paddingLeft + paddingRight + params.leftMargin + params.rightMargin, params.width);
                int heightSpec = getChildMeasureSpec(parentHeightSpec,
                        paddingTop + paddingBottom + params.topMargin + params.bottomMargin, params.height);
                v.measure(widthSpec, heightSpec);
            }
        }
    }

    private boolean mFirstLayout = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        Log.e("jsp", "onLayout");

        int height = getHeight();

        mDragRange = height - mDragViewHeight;

        if (mFirstLayout) {
            mTop = mDragRange;
            if (mMetaphor != null) {
                mMetaphor.setSelected(true);
            }
        }

        mDragTargetView.layout(0, mTop, r, mTop + mDragViewHeight);

        int contentViewTop = mTop + mDragViewHeight;
        int contentViewBottom = height;

        mContentView.layout(0, contentViewTop, r, contentViewBottom);

        mFirstLayout = false;
    }

    private int mTop;
    private int mDragRange;

    private float mDragOffset;
    private class DragHelperCallBack extends ViewDragHelper.Callback{
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragTargetView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTop = top;

            mDragOffset = (float) top / mDragRange;

            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mDragRange;
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mDragTargetView.getHeight();

            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }
    }

}
