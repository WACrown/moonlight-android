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
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class ConfirmDeleteControllerLayoutPreference extends DialogPreference {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmDeleteControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ConfirmDeleteControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConfirmDeleteControllerLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmDeleteControllerLayoutPreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateDialogView() {
        if (PreferenceConfiguration.readPreferences(getContext()).onscreenController){
            String currentLayoutName = SelectControllerLayoutHelp.loadSingleLayoutNameShow(getContext(), SelectControllerLayoutHelp.getCurrentController(getContext()));
            this.setDialogTitle(getContext().getResources().getString(R.string.dialog_title_delete_controller_layout));
            this.setDialogMessage(getContext().getResources().getString(R.string.dialog_text_delete_controller_layout) + currentLayoutName);
            return super.onCreateDialogView();
        } else if (PreferenceConfiguration.readPreferences(getContext()).onscreenKeyboard) {
            String currentLayoutName = SelectKeyboardLayoutHelp.loadSingleLayoutNameShow(getContext(), SelectKeyboardLayoutHelp.getCurrentController(getContext()));
            this.setDialogTitle(getContext().getResources().getString(R.string.dialog_title_delete_controller_layout));
            this.setDialogMessage(getContext().getResources().getString(R.string.dialog_text_delete_controller_layout) + currentLayoutName);
            return super.onCreateDialogView();
        }
        return null;
    }

    public void onClick(DialogInterface dialog, int which) {

        if (PreferenceConfiguration.readPreferences(getContext()).onscreenController){
            if (which == DialogInterface.BUTTON_POSITIVE) {
                int flag = SelectControllerLayoutHelp.deleteLayout(getContext(), SelectControllerLayoutHelp.getCurrentController(getContext()));
                if ( flag == 0 ){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_delete_controller_layout_success), Toast.LENGTH_SHORT).show();
                } else if ( flag == 2 ){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.dialog_text_delete_controller_layout_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }


            }
        } else if (PreferenceConfiguration.readPreferences(getContext()).onscreenKeyboard) {

            if (which == DialogInterface.BUTTON_POSITIVE) {
                int flag = SelectKeyboardLayoutHelp.deleteLayout(getContext(), SelectKeyboardLayoutHelp.getCurrentController(getContext()));
                if ( flag == 0 ){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_delete_controller_layout_success), Toast.LENGTH_SHORT).show();
                } else if ( flag == 2 ){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.dialog_text_delete_controller_layout_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }


            }
        }


    }
}
