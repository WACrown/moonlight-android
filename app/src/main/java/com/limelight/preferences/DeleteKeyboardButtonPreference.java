package com.limelight.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.limelight.utils.EditKeyboardLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class DeleteKeyboardButtonPreference extends EditTextPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DeleteKeyboardButtonPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DeleteKeyboardButtonPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DeleteKeyboardButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DeleteKeyboardButtonPreference(Context context) {
        super(context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            String keyName = getEditText().getText().toString();
            boolean isExist = EditKeyboardLayoutHelp.getAllButtonSet(getContext(), SelectKeyboardLayoutHelp.loadSingleLayoutName(getContext(),SelectKeyboardLayoutHelp.getCurrentNum(getContext()))).contains(keyName);
            if (isExist) {
                EditKeyboardLayoutHelp.deleteKeyboardButton(getContext(),keyName,SelectKeyboardLayoutHelp.loadSingleLayoutName(getContext(),SelectKeyboardLayoutHelp.getCurrentNum(getContext())));
                Toast.makeText(getContext(), "myString  删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "myString 3 删除失败，没有该按钮", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
