package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class GlassEditorView extends ViewGroup {

    public GlassEditorView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return ev.getAction() != MotionEvent.ACTION_UP;
    }
}
