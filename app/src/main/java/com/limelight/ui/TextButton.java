package com.limelight.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.limelight.binding.input.virtual_controller.VirtualControllerElementAdder;

public class TextButton extends Button {

    private final int PRESSED_COLOR = 1;
    private final int NORMAL_COLOR = 1;
    private final int DISABLE_COLOR = 1;
    private boolean isPressed = false;
    private final TextButton self;

    public TextButton(Context context, VirtualControllerElementAdder virtualControllerElementAdder) {
        super(context);
        self = this;
        setBackgroundColor(NORMAL_COLOR);
        setAlpha(0.3f);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                virtualControllerElementAdder.whichIsSelected(self);
            }
        });
    }

    public void setIsPressed(boolean isPressed){
        this.isPressed = isPressed;
        setBackgroundColor(PRESSED_COLOR);
    }

    public boolean getIsPressed(){
        return isPressed;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackgroundColor(NORMAL_COLOR);
        } else {
            setBackgroundColor(DISABLE_COLOR);
        }

    }
}
