package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogRootLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogView;

public class MyCustomDialogBuilder extends QMUIDialog.CustomDialogBuilder {

    public void setLayoutParamSetter(MyCustomDialogBuilder.layoutParamSetter layoutParamSetter) {
        this.layoutParamSetter = layoutParamSetter;
    }

    private layoutParamSetter layoutParamSetter;

    public interface layoutParamSetter{
        void operation(View layout);
    }

    public MyCustomDialogBuilder(Context context) {
        super(context);

    }

    @Override
    protected void configRootLayout(@NonNull QMUIDialogRootLayout rootLayout) {
        rootLayout.setInsetHor(-200);
        rootLayout.setMaxWidth(1000);
    }

    @Override
    protected ConstraintLayout.LayoutParams onCreateContentLayoutParams(@NonNull Context context) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.constrainedHeight = true;
        return lp;
    }



    @Nullable
    @Override
    protected View onCreateContent(QMUIDialog dialog, QMUIDialogView parent, Context context) {
        View layout = super.onCreateContent(dialog,parent,context);
        if (layoutParamSetter != null){
            layoutParamSetter.operation(layout);
        }
        return layout;
    }

}
