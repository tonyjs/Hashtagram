package com.tonyjs.hashtagram.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

/**
 * Created by tony.park on 14. 12. 2..
 */
public class BasicRecyclerView extends RecyclerView {
    public interface OnItemClickListener {
        public void onItemClick(BasicRecyclerView parent, View child, int position, long id);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(BasicRecyclerView parent, View child, int position, long id);
    }

    private ItemClickGestureDetector mGestureDetector;
    public BasicRecyclerView(Context context) {
        super(context);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    public BasicRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    public BasicRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    private void init() {
        mGestureDetector =
                new ItemClickGestureDetector(getContext(), new ItemClickGestureListener());
        setClickable(true);
    }

    private View mEmptyView;

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        if (mEmptyView != null) {
            Adapter adapter = getAdapter();
            mEmptyView.setVisibility(
                    (adapter == null || adapter.getItemCount() <= 0) ?
                            View.VISIBLE : View.INVISIBLE
            );
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!isScrollModeForcibly()) {
            updateScrollIndicator();
        }
    }

    private static final int SCROLL_MODE_FORCIBLY = 0;
    private static final int SCROLL_MODE_AUTO = 1;
    private int mScrollMode = SCROLL_MODE_AUTO;

    private void setScrollMode(int scrollMode) {
        mScrollMode = scrollMode;
    }

    private boolean isScrollModeForcibly() {
        return mScrollMode == SCROLL_MODE_FORCIBLY;
    }

    private View mScrollIndicator;

    public void setScrollIndicator(View view) {
        setScrollIndicator(view, 0, true);
    }

    public void setScrollIndicator(View view, int position) {
        setScrollIndicator(view, position, true);
    }

    public void setScrollIndicator(View view, boolean smoothScroll) {
        setScrollIndicator(view, 0, smoothScroll);
    }

    public void setScrollIndicator(View view, final int position, final boolean smoothScroll) {
        mScrollIndicator = view;
        mScrollIndicator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setScrollMode(SCROLL_MODE_FORCIBLY);
                if (smoothScroll) {
                    BasicRecyclerView.this.smoothScrollToPosition(position);
                } else {
                    BasicRecyclerView.this.scrollToPosition(position);
                }
                v.setVisibility(View.GONE);
            }
        });
        updateScrollIndicator();
    }

    private void updateScrollIndicator() {
        if (mScrollIndicator == null) {
            return;
        }

        boolean visibleIndicator = false;
        if (getChildCount() > 0) {
            View firstView = getChildAt(0);
            LayoutManager layoutManager = getLayoutManager();
            int position = layoutManager.getPosition(firstView);
            visibleIndicator = position != 0 || firstView.getTop() < getPaddingTop();
        }

        mScrollIndicator.setVisibility(visibleIndicator ? View.VISIBLE : View.GONE);
        setScrollMode(SCROLL_MODE_AUTO);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public boolean hasAdapter() {
        return getAdapter() != null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!isAttachedToWindow() || !hasAdapter()) {
            return false;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        setScrollMode(SCROLL_MODE_AUTO);
        mGestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean isAttachedToWindow() {
        if (Build.VERSION.SDK_INT >= 19) {
            return super.isAttachedToWindow();
        } else {
            return getHandler() != null;
        }
    }

    private boolean handleItemClick(View child, int position, long id) {
        if (mOnItemClickListener == null) {
            return false;
        }

        child.playSoundEffect(SoundEffectConstants.CLICK);
        mOnItemClickListener.onItemClick(this, child, position, id);
        return true;
    }

    private boolean handleItemLongClick(View child, int position, long id) {
        if (mOnItemLongClickListener == null) {
            return false;
        }

        child.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        mOnItemLongClickListener.onItemLongClick(this, child, position, id);
        return true;
    }

    private class ItemClickGestureDetector extends GestureDetectorCompat {

        ItemClickGestureListener mListener;
        public ItemClickGestureDetector(Context context, ItemClickGestureListener listener) {
            super(context, listener);
            mListener = listener;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            boolean handled = super.onTouchEvent(event);
            int action = event.getAction() & MotionEventCompat.ACTION_MASK;
            if (action == MotionEvent.ACTION_UP) {
                mListener.dispatchSingleTapUpIfNeed(event);
            }
            return handled;
        }
    }

    private class ItemClickGestureListener extends GestureDetector.SimpleOnGestureListener {
        private View mTargetView;
        public void dispatchSingleTapUpIfNeed(MotionEvent event) {
            if (mTargetView != null) {
                onSingleTapUp(event);
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mTargetView = findChildViewUnder(e.getX(), e.getY());
            return mTargetView != null;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (mTargetView != null) {
                mTargetView.setPressed(true);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            boolean handled = false;
            if (mTargetView != null) {
                mTargetView.setPressed(false);
                int position = getChildPosition(mTargetView);
                long id = getAdapter().getItemId(position);
                handled = handleItemClick(mTargetView, position, id);
                mTargetView = null;
            }
            return handled;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mTargetView != null) {
                mTargetView.setPressed(false);
                mTargetView = null;
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mTargetView == null) {
                return;
            }
            int position = getChildPosition(mTargetView);
            long id = getAdapter().getItemId(position);
            boolean handled = handleItemLongClick(mTargetView, position, id);
            if (handled) {
                mTargetView.setPressed(false);
                mTargetView = null;
            }
        }
    }
}
