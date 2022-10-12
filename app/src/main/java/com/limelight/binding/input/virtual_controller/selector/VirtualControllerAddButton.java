package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
    private final List<String> keyList = Arrays.asList("K-A", "K-B", "K-C", "K-D", "K-E", "K-F", "K-G", "K-H", "K-I", "K-J", "K-K", "K-L", "K-M", "K-N", "K-O", "K-P", "K-Q", "K-R", "K-S", "K-T", "K-U", "K-V", "K-W", "K-X", "K-Y", "K-Z",
            "K-ESC","K-CTRLL" , "K-SHIFTL", "K-CTRLR" , "K-SHIFTR", "K-ALTL"  , "K-ALTR"  , "K-ENTER" , "K-KBACK"  , "K-SPACE" , "K-TAB"   , "K-CAPS"  , "K-WIN", "K-DEL", "K-INS", "K-HOME", "K-END", "K-PGUP", "K-PGDN", "K-BREAK", "K-SLCK", "K-PRINT", "K-UP", "K-DOWN", "K-LEFT", "K-RIGHT",
            "K-1", "K-2", "K-3", "K-4", "K-5", "K-6", "K-7", "K-8", "K-9", "K-0", "K-F1", "K-F2", "K-F3", "K-F4", "K-F5", "K-F6", "K-F7", "K-F8", "K-F9", "K-F10", "K-F11", "K-F12",
            "K-~", "K-_", "K-=", "K-[", "K-]", "K-\\", "K-;", "\"", "K-<", "K->", "K-/",
            "K-NUM1", "K-NUM2", "K-NUM3", "K-NUM4", "K-NUM5", "K-NUM6", "K-NUM7", "K-NUM8", "K-NUM9", "K-NUM0", "K-NUM.", "K-NUM+", "K-NUM_", "K-NUM*", "K-NUM/", "K-NUMENT", "K-NUMLCK",
            "G-GA", "G-GB", "G-GX", "G-GY", "G-PU","G-PD","G-PL","G-PR","G-LT", "G-RT", "G-LB", "G-RB", "G-LSB", "G-RSB", "G-START","G-BACK","G-LSU","G-LSD","G-LSL","G-LSR","G-RSU","G-RSD","G-RSL","G-RSR",
            "M-ML", "M-MR", "M-MM", "M-M1", "M-M2");

    public VirtualControllerAddButton(Context context, FrameLayout layout, int leftMargin, int topMargin) {
        super(context);
        this.frame_layout = layout;
        this.context = context;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.setAdapter(new AdapterSelector(context,keyList));
        this.setVisibility(View.INVISIBLE);
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

    public List<String> getKeyList() {
        return keyList;
    }


}


