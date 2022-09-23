package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;
import com.limelight.ui.AdapterSelector;
import com.limelight.utils.controller.LayoutSelectHelper;

import java.util.Arrays;
import java.util.List;

public class VirtualControllerKeyTypeSelector extends Spinner {

    private Context context;
    private FrameLayout frame_layout;
    private final List<String> typeList = Arrays.asList("BUTTON", "PAD", "STICK");

    public VirtualControllerKeyTypeSelector(Context context, FrameLayout layout) {
        super(context);
        this.frame_layout = layout;
        this.context = context;
        this.setAdapter(new AdapterSelector(context,typeList));
        this.setSelection(0);
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
        params.leftMargin = (int)(screen.widthPixels*0.4f);
        params.topMargin = (int)(screen.heightPixels*0.1f);
        frame_layout.addView(this, params);
    }
}
