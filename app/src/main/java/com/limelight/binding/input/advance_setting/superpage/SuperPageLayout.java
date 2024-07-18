package com.limelight.binding.input.advance_setting.superpage;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class SuperPageLayout extends FrameLayout {

    public interface DoubleFingerSwipeListener{
        void onRightSwipe();
        void onLeftSwipe();
    }

    private static final int SWIPE_THRESHOLD = 70;
    private float startX;
    private boolean isTwoFingerSwipe = false;
    private boolean isSwipeActionDone = false;
    private DoubleFingerSwipeListener doubleFingerSwipeListener;


    public SuperPageLayout(Context context) {
        super(context);
    }

    public SuperPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SuperPageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                isTwoFingerSwipe = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (ev.getPointerCount() == 2) {
                    isTwoFingerSwipe = true;
                    startX = ev.getX(1);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTwoFingerSwipe && ev.getPointerCount() == 2) {
                    float diffX = ev.getX(1) - startX;
                    if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                        // 拦截双指滑动事件
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (ev.getPointerCount() == 2) {
                    isTwoFingerSwipe = false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTwoFingerSwipe && doubleFingerSwipeListener != null) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    float diffX = event.getX(1) - startX;
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && !isSwipeActionDone) {
                        if (diffX > 0) {
                            // 处理右滑操作
                            doubleFingerSwipeListener.onRightSwipe();
                        } else {
                            // 处理左滑操作
                            doubleFingerSwipeListener.onLeftSwipe();
                        }
                        isSwipeActionDone = true;
                    }
                    return true; // 处理双指滑动事件
                case MotionEvent.ACTION_POINTER_UP:
                    isTwoFingerSwipe = false;
                    isSwipeActionDone = false;
                    break;
            }
        } else {
            // 单指操作传递给子控件
            return super.onTouchEvent(event);
        }
        return true;
    }


    public void setDoubleFingerSwipeListener(DoubleFingerSwipeListener doubleFingerSwipeListener){
        this.doubleFingerSwipeListener = doubleFingerSwipeListener;
    }

}
