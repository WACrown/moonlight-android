package com.limelight.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

import java.util.List;

public class MenuItemLinearLayout extends LinearLayout {

    private TextView textView;
    private OnClickAndBackListener onClickAndBackListener;

    public abstract interface OnClickAndBackListener extends OnClickListener {
        public abstract void callback();
    }

    public MenuItemLinearLayout(Context context) {
        super(context);
    }

    public MenuItemLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuItemLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuItemLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof OnClickAndBackListener){
            this.onClickAndBackListener = (OnClickAndBackListener) l;
        }
        super.setOnClickListener(l);
    }

    public void runCallback(){
        if (onClickAndBackListener != null){
            onClickAndBackListener.callback();
        }

    }


    public void setTextView(TextView textView){
        this.textView = textView;
    }

    public void setText(String text){

        if (textView != null){
            textView.setText(text);
        }
    }

    public String getText(){
        if (textView != null){
            return textView.getText().toString();
        }
        return null;
    }

}
