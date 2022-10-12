package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.ui.AdapterSelector;

import java.util.Arrays;
import java.util.List;

public class VirtualControllerTypeSelector extends Spinner {

    private final Context context;
    private final FrameLayout frame_layout;
    private final VirtualControllerAddButton buttonUpSelector;
    private final VirtualControllerAddButton buttonDownSelector;
    private final VirtualControllerAddButton buttonLeftSelector;
    private final VirtualControllerAddButton buttonRightSelector;
    private final VirtualControllerAddButton buttonSelector;
    private final VirtualControllerFuncSelector funcSelector;
    private final VirtualControllerTypeSelector virtualControllerTypeSelector;
    private final List<String> typeList = Arrays.asList("BUTTON", "PAD", "STICK");

    public VirtualControllerTypeSelector(Context mcontext, FrameLayout layout,
                                         VirtualControllerFuncSelector mFuncSelector,
                                         VirtualControllerAddButton mButtonSelector,
                                         VirtualControllerAddButton mButtonUpSelector,
                                         VirtualControllerAddButton mButtonDownSelector,
                                         VirtualControllerAddButton mButtonLeftSelector,
                                         VirtualControllerAddButton mButtonRightSelector) {
        super(mcontext);
        this.frame_layout = layout;
        this.context = mcontext;
        this.funcSelector = mFuncSelector;
        this.buttonUpSelector = mButtonUpSelector;
        this.buttonDownSelector = mButtonDownSelector;
        this.buttonLeftSelector = mButtonLeftSelector;
        this.buttonRightSelector = mButtonRightSelector;
        this.buttonSelector = mButtonSelector;
        virtualControllerTypeSelector = this;
        this.setAdapter(new AdapterSelector(context,typeList));
        this.setVisibility(View.INVISIBLE);
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (virtualControllerTypeSelector.getVisibility() == VISIBLE){
                    if (i == 0){
                        buttonSelector.setAdapter(new AdapterSelector(context, buttonSelector.getKeyList()));
                        buttonSelector.setVisibility(VISIBLE);
                        funcSelector.setVisibility(VISIBLE);

                        buttonUpSelector.setVisibility(INVISIBLE);
                        buttonDownSelector.setVisibility(INVISIBLE);
                        buttonLeftSelector.setVisibility(INVISIBLE);
                        buttonRightSelector.setVisibility(INVISIBLE);
                    } else if (i == 1) {
                        buttonSelector.setAdapter(new AdapterSelector(context, buttonSelector.getKeyList()));
                        buttonSelector.setVisibility(INVISIBLE);
                        funcSelector.setVisibility(INVISIBLE);

                        buttonUpSelector.setVisibility(VISIBLE);
                        buttonDownSelector.setVisibility(VISIBLE);
                        buttonLeftSelector.setVisibility(VISIBLE);
                        buttonRightSelector.setVisibility(VISIBLE);
                    } else if (i == 2) {
                        buttonSelector.setAdapter(new AdapterSelector(context, buttonSelector.getKeyList()));
                        buttonSelector.setVisibility(VISIBLE);
                        funcSelector.setVisibility(INVISIBLE);

                        buttonUpSelector.setVisibility(VISIBLE);
                        buttonDownSelector.setVisibility(VISIBLE);
                        buttonLeftSelector.setVisibility(VISIBLE);
                        buttonRightSelector.setVisibility(VISIBLE);
                    } else {
                        buttonSelector.setVisibility(INVISIBLE);
                        funcSelector.setVisibility(INVISIBLE);

                        buttonUpSelector.setVisibility(INVISIBLE);
                        buttonDownSelector.setVisibility(INVISIBLE);
                        buttonLeftSelector.setVisibility(INVISIBLE);
                        buttonRightSelector.setVisibility(INVISIBLE);
                    }
                } else {
                    buttonSelector.setVisibility(INVISIBLE);
                    funcSelector.setVisibility(INVISIBLE);

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
            this.setSelection(0);
            buttonSelector.setAdapter(new AdapterSelector(context, buttonSelector.getKeyList()));
            buttonSelector.setVisibility(VISIBLE);
            funcSelector.setVisibility(VISIBLE);
        } else {
            buttonSelector.setVisibility(INVISIBLE);
            funcSelector.setVisibility(INVISIBLE);
        }
        buttonUpSelector.setVisibility(INVISIBLE);
        buttonDownSelector.setVisibility(INVISIBLE);
        buttonLeftSelector.setVisibility(INVISIBLE);
        buttonRightSelector.setVisibility(INVISIBLE);
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
