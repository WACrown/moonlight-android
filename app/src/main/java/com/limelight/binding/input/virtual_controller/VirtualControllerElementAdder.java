package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.FrameLayout;

import com.limelight.ui.TextButton;

public class VirtualControllerElementAdder {

    private final int SELECT_LAYOUT = 0;
    private final int SELECT_ELEMENT_TYPE = 1;
    private final int SELECT_BUTTON_FUNC = 2;
    private final int SELECT_KEY_CENTER = 3;
    private final int SELECT_KEY_UP = 4;
    private final int SELECT_KEY_DOWN = 5;
    private final int SELECT_KEY_LEFT = 6;
    private final int SELECT_KEY_RIGHT = 7;
    private final TextButton[] textButtons = new TextButton[8];
    private final DisplayMetrics screen;
    private final FrameLayout frameLayout;

    public VirtualControllerElementAdder(Context context, FrameLayout frameLayout) {
        screen = context.getResources().getDisplayMetrics();
        this.frameLayout = frameLayout;
        for (int i = 0; i < 8; i++){
            textButtons[i] = new TextButton(context,this);
        }
    }

    public void whichIsSelected(TextButton textButton){
        for (TextButton textButtonSingle : textButtons){
            textButtonSingle.setIsPressed(false);
        }
        textButton.setEnabled(true);
    }

    private void refreshLayout(){
        FrameLayout.LayoutParams params = null;
        params = new FrameLayout.LayoutParams((int)(screen.widthPixels*0.4f), (int)(screen.heightPixels*0.1f));
        params.leftMargin = (int)(screen.widthPixels*0.3f);
        params.topMargin = (int)(screen.heightPixels*0.05f);
        frameLayout.addView(textButtons[SELECT_LAYOUT],params);

        params = new FrameLayout.LayoutParams((int)(screen.widthPixels*0.18f), (int)(screen.heightPixels*0.1f));
        params.leftMargin = (int)(screen.widthPixels*0.3f);
        params.topMargin = (int)(screen.heightPixels*0.175f);
        frameLayout.addView(textButtons[SELECT_ELEMENT_TYPE],params);

        params.leftMargin = (int)(screen.widthPixels*0.52f);
        params.topMargin = (int)(screen.heightPixels*0.175f);
        frameLayout.addView(textButtons[SELECT_BUTTON_FUNC],params);

        params = new FrameLayout.LayoutParams((int)(screen.widthPixels*0.18f), (int)(screen.heightPixels*0.1f));
        params.leftMargin = (int)(screen.widthPixels*0.3f);
        params.topMargin = (int)(screen.heightPixels*0.175f);
        frameLayout.addView(textButtons[SELECT_KEY_UP],params);

        params.leftMargin = (int)(screen.widthPixels*0.52f);
        params.topMargin = (int)(screen.heightPixels*0.175f);
        frameLayout.addView(textButtons[SELECT_LAYOUT],params);



    }




}
