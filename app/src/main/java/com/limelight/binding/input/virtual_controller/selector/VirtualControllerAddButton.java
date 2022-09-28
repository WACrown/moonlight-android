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
    private final List<String> keyList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "CTRLL" , "SHIFTL", "CTRLR" , "SHIFTR", "ALTL"  , "ALTR"  , "ENTER" , "BACK"  , "SPACE" , "TAB"   , "CAPS"  , "WIN", "DEL", "INS", "HOME", "END", "PGUP", "PGDN", "BREAK", "SLCK", "PRINT", "UP", "DOWN", "LEFT", "RIGHT",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12",
            "~", "_", "=", "[", "]", "\\", ";", "\"", "<", ">", "/",
            "NUM1", "NUM2", "NUM3", "NUM4", "NUM5", "NUM6", "NUM7", "NUM8", "NUM9", "NUM0", "NUM.", "NUM+", "NUM_", "NUM*", "NUM/", "NUMENT", "NUMLCK");

    private final List<String> GPList = Arrays.asList("GA", "GB", "GX", "GY", "PAD", "LS", "RS", "LB", "RB", "LSB", "RSB", "START","BACK","LT","RT");



    private final List<String> mouseList = Arrays.asList("ML", "MR", "MM", "MB1", "MB2");

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

    public List<String> getGPList() {
        return GPList;
    }

    public List<String> getMouseList() {
        return mouseList;
    }
}


