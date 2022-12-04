package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogRootLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogView;

public class MyCustomDialogBuilder extends QMUIDialog.CustomDialogBuilder {

    public static final int VERTICAL_CENTER = -1;
    public static final int HORIZONTAL_CENTER = -1;
    private int positionX = HORIZONTAL_CENTER;
    private int positionY = VERTICAL_CENTER;



    private boolean isTransportTouch = false;

    public void setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
    }

    private float dimAmount = 0.4f;


    public void setLayoutParamSetter(MyCustomDialogBuilder.layoutParamSetter layoutParamSetter) {
        this.layoutParamSetter = layoutParamSetter;
    }

    private layoutParamSetter layoutParamSetter;

    public interface layoutParamSetter{
        void operation(View layout,QMUIDialog dialog);
    }

    public MyCustomDialogBuilder(Context context) {
        super(context);

    }

    @Override
    protected void configRootLayout(@NonNull QMUIDialogRootLayout rootLayout) {
        int screenHeight = QMUIDisplayHelper.getScreenHeight(getBaseContext());
        rootLayout.setInsetHor(-QMUIDisplayHelper.dp2px(getBaseContext(),screenHeight));
        rootLayout.setMaxWidth(QMUIDisplayHelper.dp2px(getBaseContext(),screenHeight));
    }

    @Override
    protected ConstraintLayout.LayoutParams onCreateContentLayoutParams(@NonNull Context context) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.constrainedHeight = true;
        return lp;
    }

    @Override
    protected void onAfterCreate(@NonNull QMUIDialog dialog, @NonNull QMUIDialogRootLayout rootLayout, @NonNull Context context) {
        Window window = dialog.getWindow();
        window.setDimAmount(dimAmount);
        if (isTransportTouch){
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        int gravity = Gravity.CENTER;
        if (positionX != HORIZONTAL_CENTER){
            gravity = (gravity & ~Gravity.CENTER_HORIZONTAL) | Gravity.START;
            lp.x = Math.max(positionX,0);
        }

        if (positionY != VERTICAL_CENTER){
            gravity = (gravity & ~Gravity.CENTER_VERTICAL) | Gravity.TOP;
            lp.y = Math.max(positionY,0);
        }
        window.setGravity(gravity);
        window.setAttributes(lp);
        super.onAfterCreate(dialog, rootLayout, context);
    }

    @Nullable
    @Override
    protected View onCreateContent(QMUIDialog dialog, QMUIDialogView parent, Context context) {
        View layout = super.onCreateContent(dialog,parent,context);
        if (layoutParamSetter != null){
            layoutParamSetter.operation(layout,dialog);
        }
        return layout;
    }

    public void setPosition(int positionX,int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setTransportTouch(boolean transportTouch) {
        isTransportTouch = transportTouch;
    }
}
