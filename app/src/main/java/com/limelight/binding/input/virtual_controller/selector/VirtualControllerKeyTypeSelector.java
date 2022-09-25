package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;
import com.limelight.ui.AdapterSelector;
import com.limelight.utils.controller.LayoutSelectHelper;

import java.util.Arrays;
import java.util.List;

public class VirtualControllerKeyTypeSelector extends Spinner {

    private Context context;
    private FrameLayout frame_layout;
    private final VirtualControllerAddButton buttonUpSelector;
    private final VirtualControllerAddButton buttonDownSelector;
    private final VirtualControllerAddButton buttonLeftSelector;
    private final VirtualControllerAddButton buttonRightSelector;
    private final VirtualControllerAddButton buttonSelector;
    private final VirtualControllerKeyTypeSelector virtualControllerKeyTypeSelector;
    private final List<String> typeList = Arrays.asList("BUTTON", "PAD", "STICK");

    public VirtualControllerKeyTypeSelector(Context context, FrameLayout layout,
                                            VirtualControllerAddButton mButtonSelector,
                                            VirtualControllerAddButton mButtonUpSelector,
                                            VirtualControllerAddButton mButtonDownSelector,
                                            VirtualControllerAddButton mButtonLeftSelector,
                                            VirtualControllerAddButton mButtonRightSelector) {
        super(context);
        this.frame_layout = layout;
        this.context = context;
        this.buttonUpSelector = mButtonUpSelector;
        this.buttonDownSelector = mButtonDownSelector;
        this.buttonLeftSelector = mButtonLeftSelector;
        this.buttonRightSelector = mButtonRightSelector;
        this.buttonSelector = mButtonSelector;
        virtualControllerKeyTypeSelector = this;
        this.setAdapter(new AdapterSelector(context,typeList));
        //this.setVisibility(View.INVISIBLE);
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("wangguan select");
                if (virtualControllerKeyTypeSelector.getVisibility() == VISIBLE){
                    if (i == 0){

                        buttonSelector.setVisibility(VISIBLE);

                        buttonUpSelector.setVisibility(INVISIBLE);
                        buttonDownSelector.setVisibility(INVISIBLE);
                        buttonLeftSelector.setVisibility(INVISIBLE);
                        buttonRightSelector.setVisibility(INVISIBLE);
                    } else if (i == 1) {
                        buttonSelector.setVisibility(INVISIBLE);

                        buttonUpSelector.setVisibility(VISIBLE);
                        buttonDownSelector.setVisibility(VISIBLE);
                        buttonLeftSelector.setVisibility(VISIBLE);
                        buttonRightSelector.setVisibility(VISIBLE);
                    } else if (i == 2) {
                        buttonSelector.setVisibility(VISIBLE);

                        buttonUpSelector.setVisibility(VISIBLE);
                        buttonDownSelector.setVisibility(VISIBLE);
                        buttonLeftSelector.setVisibility(VISIBLE);
                        buttonRightSelector.setVisibility(VISIBLE);
                    } else {
                        buttonSelector.setVisibility(INVISIBLE);

                        buttonUpSelector.setVisibility(INVISIBLE);
                        buttonDownSelector.setVisibility(INVISIBLE);
                        buttonLeftSelector.setVisibility(INVISIBLE);
                        buttonRightSelector.setVisibility(INVISIBLE);
                    }
                } else {
                    buttonSelector.setVisibility(INVISIBLE);

                    buttonUpSelector.setVisibility(INVISIBLE);
                    buttonDownSelector.setVisibility(INVISIBLE);
                    buttonLeftSelector.setVisibility(INVISIBLE);
                    buttonRightSelector.setVisibility(INVISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            this.setSelection(1);
            this.setSelection(0);
        } else {
            buttonSelector.setVisibility(INVISIBLE);
            buttonUpSelector.setVisibility(INVISIBLE);
            buttonDownSelector.setVisibility(INVISIBLE);
            buttonLeftSelector.setVisibility(INVISIBLE);
            buttonRightSelector.setVisibility(INVISIBLE);
        }

    }

    public void refreshLayout(){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int spinnerHigh = (int)(screen.heightPixels*0.1f);
        int spinnerWidth = (int)(screen.widthPixels*0.2f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(spinnerWidth, spinnerHigh);
        params.leftMargin = (int)(screen.widthPixels*0.275f);
        params.topMargin = (int)(screen.heightPixels*0.1f);
        frame_layout.addView(this, params);
    }
}
