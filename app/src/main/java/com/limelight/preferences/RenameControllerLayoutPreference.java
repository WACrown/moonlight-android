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
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class RenameControllerLayoutPreference extends EditTextPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RenameControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RenameControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RenameControllerLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RenameControllerLayoutPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateDialogView() {
        if (PreferenceConfiguration.readPreferences(getContext()).onscreenController){
            setDialogTitle(getContext().getResources().getString(R.string.title_rename_controller_layout));
            setText(SelectControllerLayoutHelp.loadSingleLayoutNameShow(getContext(), SelectControllerLayoutHelp.getCurrentController(getContext())));
            return super.onCreateDialogView();
        } else if (PreferenceConfiguration.readPreferences(getContext()).onscreenKeyboard) {
            setDialogTitle(getContext().getResources().getString(R.string.title_rename_controller_layout));
            setText(SelectKeyboardLayoutHelp.loadSingleLayoutNameShow(getContext(), SelectKeyboardLayoutHelp.getCurrentController(getContext())));
            return super.onCreateDialogView();
        }
        return null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (PreferenceConfiguration.readPreferences(getContext()).onscreenController){
            if (which == DialogInterface.BUTTON_POSITIVE) {
                int flag = SelectControllerLayoutHelp.renameLayout(getContext(), SelectControllerLayoutHelp.getCurrentController(getContext()),getEditText().getText().toString());

                if (flag == 0){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_success), Toast.LENGTH_SHORT).show();
                } else if (flag == 2){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_failed_2), Toast.LENGTH_SHORT).show();
                } else if (flag == 3){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_failed_3), Toast.LENGTH_SHORT).show();
                } else {

                }

            }
        } else if (PreferenceConfiguration.readPreferences(getContext()).onscreenKeyboard) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                int flag = SelectKeyboardLayoutHelp.renameLayout(getContext(), SelectKeyboardLayoutHelp.getCurrentController(getContext()),getEditText().getText().toString());

                if (flag == 0){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_success), Toast.LENGTH_SHORT).show();
                } else if (flag == 2){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_failed_2), Toast.LENGTH_SHORT).show();
                } else if (flag == 3){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_rename_controller_layout_failed_3), Toast.LENGTH_SHORT).show();
                } else {

                }

            }
        }



    }
}
