package com.limelight.preferences.Controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.utils.SelectLayoutHelp;

public class ConfirmResetControllerLayoutPreference extends DialogPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmResetControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ConfirmResetControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConfirmResetControllerLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmResetControllerLayoutPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateDialogView() {
        String currentLayoutName = SelectLayoutHelp.loadSingleLayoutName(getContext(),SelectLayoutHelp.getCurrentNum(getContext()));
        this.setDialogTitle(getContext().getResources().getString(R.string.dialog_title_reset_controller_layout));
        this.setDialogMessage(getContext().getResources().getString(R.string.dialog_text_reset_controller_layout) + currentLayoutName);
        return super.onCreateDialogView();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            SelectLayoutHelp.resetLayout(getContext(),SelectLayoutHelp.getCurrentNum(getContext()));
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_reset_controller_layout_success), Toast.LENGTH_SHORT).show();
        }
    }
}
