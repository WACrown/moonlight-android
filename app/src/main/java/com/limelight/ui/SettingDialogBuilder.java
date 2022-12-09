package com.limelight.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import com.limelight.R;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.layout.QMUILinearLayout;

public class SettingDialogBuilder {

    private final Context context;
    private final FrameLayout frameLayout;

    public SettingDialogBuilder(Context context, FrameLayout frameLayout) {
        this.context = context;
        this.frameLayout = frameLayout;
    }

    public View createDialog(@LayoutRes int resID){
        FrameLayout dialogRoot = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.setting_dialog,null);
        dialogRoot.setVisibility(View.INVISIBLE);
        frameLayout.addView(dialogRoot);
        ViewGroup dialog = (ViewGroup) dialogRoot.getChildAt(1);
        dialog.addView(LayoutInflater.from(context).inflate(resID,null));
        return dialogRoot;
    }





}
