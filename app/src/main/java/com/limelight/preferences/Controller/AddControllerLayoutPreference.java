package com.limelight.preferences.Controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.utils.SelectLayoutHelp;

public class AddControllerLayoutPreference extends EditTextPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AddControllerLayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddControllerLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddControllerLayoutPreference(Context context) {
        super(context);
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {

            int flag = SelectLayoutHelp.addLayout(getContext(),getEditText().getText().toString());

            if (flag == 0){
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_add_controller_layout_success), Toast.LENGTH_SHORT).show();
            } else if (flag == 2){
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_add_controller_layout_failed_2), Toast.LENGTH_SHORT).show();
            } else if (flag == 3){
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_add_controller_layout_failed_3), Toast.LENGTH_SHORT).show();
            } else {

            }

        }

    }
}
