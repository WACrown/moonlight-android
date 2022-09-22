package com.limelight.preferences.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.limelight.utils.controller.LayoutEditHelper;

public class AddKeyboardButtonPreference extends EditTextPreference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddKeyboardButtonPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AddKeyboardButtonPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddKeyboardButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddKeyboardButtonPreference(Context context) {
        super(context);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            int flag = LayoutEditHelper.addButton(getContext(), getEditText().getText().toString());
            if (flag == 0){
                Toast.makeText(getContext(), "myString 1 创建成功", Toast.LENGTH_SHORT).show();
            } else if (flag == -1){
                Toast.makeText(getContext(), "myString 2 创建失败，已存在该按钮", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "myString 3 创建失败，名字非法", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
