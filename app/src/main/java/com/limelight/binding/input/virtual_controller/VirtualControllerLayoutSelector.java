package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.ui.AdapterSelector;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class VirtualControllerLayoutSelector extends Spinner{

    private Context context;
    private FrameLayout frame_layout;

    public VirtualControllerLayoutSelector(Context context, FrameLayout layout) {
        super(context);
        this.frame_layout = layout;
        this.context = context;
        this.setAdapter(new AdapterSelector(context));
        if (PreferenceConfiguration.readPreferences(context).onscreenController){
            this.setSelection(SelectControllerLayoutHelp.getCurrentNum(context));
        } else if (PreferenceConfiguration.readPreferences(context).onscreenKeyboard) {
            this.setSelection(SelectKeyboardLayoutHelp.getCurrentNum(context));
        }

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
