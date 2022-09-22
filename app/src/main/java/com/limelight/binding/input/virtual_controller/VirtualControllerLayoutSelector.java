package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.ui.AdapterSelector;
import com.limelight.utils.controller.LayoutSelectHelper;

public class VirtualControllerLayoutSelector extends Spinner{

    private Context context;
    private FrameLayout frame_layout;

    public VirtualControllerLayoutSelector(Context context, FrameLayout layout) {
        super(context);
        this.frame_layout = layout;
        this.context = context;
        this.setAdapter(new AdapterSelector(context));
        this.setSelection(LayoutSelectHelper.getCurrentLayoutNum(getContext()));
        this.setVisibility(View.INVISIBLE);
    }


    public void refreshLayout(){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int spinnerHigh = (int)(screen.heightPixels*0.1f);
        int spinnerWidth = (int)(screen.widthPixels*0.4f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(spinnerWidth, spinnerHigh);
        params.leftMargin = (int)(screen.widthPixels*0.3f);
        params.topMargin = 50;
        frame_layout.addView(this, params);
    }





}
