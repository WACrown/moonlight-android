package com.limelight.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MenuLinearLayout extends LinearLayout {

    private boolean childClickable = true;

    public MenuLinearLayout(Context context) {
        super(context);
    }

    public MenuLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        childClickable = !clickable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!childClickable){
            return true;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
