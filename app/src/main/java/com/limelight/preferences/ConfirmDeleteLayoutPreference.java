package com.limelight.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.LayoutHelper;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class ConfirmDeleteLayoutPreference extends DialogPreference {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmDeleteLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ConfirmDeleteLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConfirmDeleteLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmDeleteLayoutPreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateDialogView() {
        String currentLayoutName = LayoutHelper.getLayoutList().get(LayoutHelper.getCurrentNum());
        this.setDialogTitle("myString 删除当前布局");
        this.setDialogMessage("myString 确定要删除当前布局: " + currentLayoutName);
        return super.onCreateDialogView();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (LayoutHelper.deleteCurrentLayout() == 0) {
                Toast.makeText(getContext(), "myString 删除布局成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "myString 删除布局失败，你至少有一个布局", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
