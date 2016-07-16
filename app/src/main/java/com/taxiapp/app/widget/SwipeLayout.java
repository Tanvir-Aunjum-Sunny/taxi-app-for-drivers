package com.taxiapp.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by ashwin on 02/01/16.
 */
public class SwipeLayout extends LinearLayout {

    public interface OnSwipeCompleteListener {
        public void execute();
    }

    public interface Function {
        public int apply(double x);
    }


    public static final double DEFAULT_SWIPE_COMPLETION_PERCENTAGE = 60;

    private boolean swipeComplete = false;
    private double swipeCompletionOffset;
    private double prevXtouch = 0;

    private DisplayMetrics displayMetrics;

    //Since swipe is done by changing the padding, preserving the actual to restore it to default.
    private int actualPaddingLeft = getPaddingLeft();
    private int actualPaddingRight = getPaddingRight();

    private OnSwipeCompleteListener onSwipeCompleteListener = null;

    private class SwipeBackAnimation extends TranslateAnimation  {

        private SwipeBackAnimation(int displacement) {
            super(displacement, 0, 0, 0);
            setDuration(500);
            setInterpolator(new DecelerateInterpolator());
        }
    }

    public SwipeLayout(Context context, AttributeSet attribSet) {
        super(context, attribSet);
        displayMetrics = getDisplayMetrics();
        setSwipeCompletionPercentage(DEFAULT_SWIPE_COMPLETION_PERCENTAGE);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        actualPaddingLeft = left;
        actualPaddingRight = right;
    }

    private Function swipeTranslation = new Function() {
        @Override
        public int apply(double swipeDistance) {
            int offset = getPaddingLeft();
            return (int) (offset + swipeDistance);
        }
    };

    public void setSwipeCompletionPercentage(double percent) {
        percent = ((percent >= 100 ? 99 : percent) / 100.0); // anything more than or equal to 100 is considered to 99% swipe
        swipeCompletionOffset = displayMetrics.widthPixels * percent;
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                prevXtouch = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!swipeComplete) {
                    moveLeftPadding(swipeTranslation.apply(event.getX() - prevXtouch));
                }
                prevXtouch = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (!swipeComplete && isSwipeComplete(swipeTranslation.apply(event.getX() - prevXtouch))) {
                    Log.d("SWIPE LAYOUT", "Swipe Complete.");
                    moveLeftPadding(displayMetrics.widthPixels);
                    swipeComplete = true;
                    if (onSwipeCompleteListener != null) {
                        onSwipeCompleteListener.execute();
                    }
                } else if (!swipeComplete) {
                    Log.d("SWIPE LAYOUT", "Swipe InComplete.");
                    restoreToDefault();
                }
        }
        return true;
    }

    public void restoreToDefault() {
        int displacement = getPaddingLeft() - actualPaddingLeft;
        setPadding(actualPaddingLeft, getPaddingTop(), actualPaddingRight, getPaddingBottom());
        this.startAnimation(new SwipeBackAnimation(displacement));
        swipeComplete = false;
    }


    private boolean isSwipeComplete(int offset) {
        return offset > swipeCompletionOffset;
    }

    private void moveLeftPadding(int offset) {
        super.setPadding(offset, getPaddingTop(), getPaddingRight() - offset, getPaddingBottom());
    }

    public OnSwipeCompleteListener getOnSwipeCompleteListener() {
        return onSwipeCompleteListener;
    }

    public void setOnSwipeCompleteListener(OnSwipeCompleteListener onSwipeCompleteListener) {
        this.onSwipeCompleteListener = onSwipeCompleteListener;
    }
}
