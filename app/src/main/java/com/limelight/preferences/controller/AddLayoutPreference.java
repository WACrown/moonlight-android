package com.limelight.preferences.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.limelight.utils.controller.LayoutAdminHelper;

public class AddLayoutPreference extends EditTextPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AddLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddLayoutPreference(Context context) {
        super(context);
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {

            if (LayoutAdminHelper.addLayout(getContext(),getEditText().getText().toString()) == 0){
                Toast.makeText(getContext(), "myString 新建布局成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "myString 新建布局失败，名字非法或布局已存在", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
