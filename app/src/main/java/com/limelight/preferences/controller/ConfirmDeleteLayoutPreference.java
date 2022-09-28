package com.limelight.preferences.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.utils.controller.LayoutAdminHelper;

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
        String currentLayoutName = LayoutAdminHelper.getCurrentLayoutName(getContext());
        this.setDialogTitle(getContext().getResources().getString(R.string.title_delete_controller_layout));
        this.setDialogMessage(getContext().getResources().getString(R.string.dialog_text_delete_controller_layout) + currentLayoutName);
        return super.onCreateDialogView();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (LayoutAdminHelper.deleteCurrentLayout(getContext()) == 0) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_delete_controller_layout_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.dialog_text_delete_controller_layout_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
