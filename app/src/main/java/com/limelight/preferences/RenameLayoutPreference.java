package com.limelight.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.LayoutHelper;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class RenameLayoutPreference extends EditTextPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RenameLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RenameLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RenameLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RenameLayoutPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateDialogView() {

        setDialogTitle(getContext().getResources().getString(R.string.title_rename_controller_layout));
        setText(LayoutHelper.getCurrentLayoutName());
        return super.onCreateDialogView();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (LayoutHelper.renameCurrentLayout(getEditText().getText().toString()) == 0) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "myString 重命名失败，已经存在改布局", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
