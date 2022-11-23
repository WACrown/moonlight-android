package com.limelight.binding.input.virtual_controller.game_setting.item;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class MyCheckableDialogBuilder extends QMUIDialog.CheckableDialogBuilder {

    private QMUIRoundButton fatherView;

    public MyCheckableDialogBuilder(Context context) {
        super(context);
    }


    public QMUIDialog show(QMUIRoundButton v) {
        fatherView = v;
        return super.show();
    }

    public void setFatherViewText(String text){
        fatherView.setText(text);
    }
}
