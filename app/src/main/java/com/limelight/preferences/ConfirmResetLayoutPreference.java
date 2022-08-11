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
import com.limelight.utils.LayoutSelectHelper;

public class ConfirmResetLayoutPreference extends DialogPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmResetLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ConfirmResetLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConfirmResetLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmResetLayoutPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateDialogView() {
        String currentLayoutName = LayoutSelectHelper.getCurrentLayoutName();
        this.setDialogTitle(getContext().getResources().getString(R.string.dialog_title_reset_controller_layout));
        this.setDialogMessage(getContext().getResources().getString(R.string.dialog_text_reset_controller_layout) + currentLayoutName);
        return super.onCreateDialogView();
    }

    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {
            LayoutSelectHelper.deleteCurrentLayout();
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_reset_controller_layout_success), Toast.LENGTH_SHORT).show();
        }

    }
}
