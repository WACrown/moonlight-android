package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.limelight.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogRootLayout;

public class MyCustomDialogBuilder extends QMUIDialog.CustomDialogBuilder {
    public MyCustomDialogBuilder(Context context) {
        super(context);

    }

    @Override
    protected void configRootLayout(@NonNull QMUIDialogRootLayout rootLayout) {
        System.out.println("wangguan rootlayout");
        rootLayout.setMinWidth(1000);
        rootLayout.setBackgroundColor(Color.RED);
    }

    @Override
    protected ConstraintLayout.LayoutParams onCreateContentLayoutParams(@NonNull Context context) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(20, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.constrainedHeight = true;
        return lp;
    }
}
