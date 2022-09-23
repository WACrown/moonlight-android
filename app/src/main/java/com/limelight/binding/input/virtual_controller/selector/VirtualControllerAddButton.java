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

public class VirtualControllerAddButton extends Spinner {

    public final static int UP = 0;
    public final static int DOWN = 0;
    public final static int LEFT = 0;
    public final static int RIGHT = 0;

    private final Context context;
    private final FrameLayout frame_layout;
    private final int leftMargin;
    private final int topMargin;
    private final List<String> keyList = Arrays.asList("A",
            "");

    public VirtualControllerAddButton(Context context, FrameLayout layout, int leftMargin, int topMargin) {
        super(context);
        this.frame_layout = layout;
        this.context = context;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.setAdapter(new AdapterSelector(context,keyList));
        this.setVisibility(View.INVISIBLE);
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void refreshLayout(){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int spinnerHigh = (int)(screen.heightPixels*0.1f);
        int spinnerWidth = (int)(screen.widthPixels*0.2f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(spinnerWidth, spinnerHigh);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        frame_layout.addView(this, params);
    }
}


