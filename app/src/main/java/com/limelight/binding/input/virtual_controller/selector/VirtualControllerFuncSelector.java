package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.ui.AdapterSelector;

import java.util.Arrays;
import java.util.List;

public class VirtualControllerFuncSelector extends Spinner {

    private final Context context;
    private final FrameLayout frame_layout;
    private final List<String> funcList = Arrays.asList("COMMON");

    public VirtualControllerFuncSelector(Context context, FrameLayout frameLayout) {
        super(context);
        this.context = context;
        this.frame_layout = frameLayout;
        setAdapter(new AdapterSelector(context,funcList));
        setVisibility(View.INVISIBLE);
    }


    public void refreshLayout(){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int spinnerHigh = (int)(screen.heightPixels*0.1f);
        int spinnerWidth = (int)(screen.widthPixels*0.2f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(spinnerWidth, spinnerHigh);
        params.leftMargin = (int)(screen.widthPixels*0.525f);
        params.topMargin = (int)(screen.heightPixels*0.1f);
        frame_layout.addView(this, params);
    }
}
