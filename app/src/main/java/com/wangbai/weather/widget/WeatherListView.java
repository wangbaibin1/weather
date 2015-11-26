package com.wangbai.weather.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ListView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wangbai.weather.util.DenstyUtil;

/**
 * Created by binwang on 2015/11/23.
 */
public class WeatherListView extends ListView {
    private float mCurrentMarginTop;
    private float mMinMoveDistance;

    public WeatherListView(Context context) {
        this(context, null);
    }

    private AccelerateInterpolator mAccelerateInterpolator = new AccelerateInterpolator(.15f);
    private int mBaseMargin;

    public WeatherListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT >= 11) {
            setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mMinMoveDistance = DenstyUtil.dip2px(context, 7);
        mBaseMargin = DenstyUtil.dip2px(context, -50);
        mCurrentMarginTop = mBaseMargin;
        onScroll(mCurrentMarginTop, false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (handleEvent(ev)) {
            return true;
        }
        return super.onTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (handleEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + DenstyUtil.dip2px(getContext(), 50));
    }

    public void onScroll(float goaldistanceY, boolean isScollOver) {
        if (isScollOver) {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(this, "y", mCurrentMarginTop);
            translateY.setInterpolator(mAccelerateInterpolator);
            translateY.setDuration(200);
            translateY.start();

        } else {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(this, "y", goaldistanceY);
            translateY.setDuration(0);
            translateY.start();
        }

    }

    public void startUpdating() {
        mCurrentMarginTop = 0;
        onScroll(mCurrentMarginTop, true);
        mIsUpdating = true;

    }

    private boolean mIsUpdating;

    public void backToBegin() {
        mCurrentMarginTop = mBaseMargin;
        onScroll(mCurrentMarginTop, true);
        mIsUpdating = false;
    }

    private float beforeY;
    private boolean mIsFilt = false;
    private float mStartY;
    private boolean isIntercept = false;

    private boolean handleEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeY = event.getRawY();
                mStartY = event.getRawY();
                return false;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mIsFilt = true;
                return false;
            case MotionEvent.ACTION_MOVE:
                if (!isIntercept && Math.abs(event.getRawY() - mStartY) <= mMinMoveDistance) {
                    return false;
                } else {
                    isIntercept = true;
                }

                if (mIsFilt) {
                    mIsFilt = false;
                    beforeY = event.getRawY();
                }

                float distance = event.getRawY() - beforeY;
                beforeY = event.getRawY();
                if (Math.abs(distance) <= 0) {
                    return false;
                }

                if (mIsUpdating) {
                    return false;
                }

                if (mCurrentMarginTop == mBaseMargin) {
                    if (getLastVisiblePosition() == getCount() - 1
                            && getChildAt(getChildCount() - 1).getBottom() <= getHeight() && distance < 0) {
                        mCurrentMarginTop = mCurrentMarginTop + distance / 2;
                        onScroll(mCurrentMarginTop, false);
                        return true;
                    } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() == 0 && distance > 0) {
                        mCurrentMarginTop = mCurrentMarginTop + distance / 2;
                        onScroll(mCurrentMarginTop, false);
                        return true;
                    }
                } else {
                    distance = distance / 2;
                    mCurrentMarginTop = mCurrentMarginTop + distance;
                    onScroll(mCurrentMarginTop, false);
                    if (mCurrentMarginTop > -mBaseMargin) {
                        mScrollListener.canUpdate(true);
                    } else {
                        mScrollListener.canUpdate(false);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isIntercept = false;
                if (mCurrentMarginTop > -mBaseMargin) {
                    mCurrentMarginTop = 0;
                    onScroll(mCurrentMarginTop, true);
                    mScrollListener.startUpdate();
                } else {
                    mCurrentMarginTop = mBaseMargin;
                    onScroll(mCurrentMarginTop, true);
                }

                return false;
        }

        return false;
    }

    public void setScrollListener(ScrollListener l) {
        mScrollListener = l;
    }

    private ScrollListener mScrollListener;

    public interface ScrollListener {
        void startUpdate();

        void canUpdate(boolean isCan);
    }

}
