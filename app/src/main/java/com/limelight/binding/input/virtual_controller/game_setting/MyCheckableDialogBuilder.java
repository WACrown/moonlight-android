package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.limelight.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MyCheckableDialogBuilder extends QMUIDialog.CheckableDialogBuilder {

    private QMUIRoundButton fatherView;
    private final String[] keyList;

    public MyCheckableDialogBuilder(Context context) {
        super(context);
        keyList = context.getResources().getStringArray(R.array.key_list);

    }


    public QMUIDialog show(@NonNull QMUIRoundButton v) {
        fatherView = v;
        int index = Arrays.binarySearch(keyList,v.getText());
        if (index != -1){
            this.setCheckedIndex(index);
        } else {
            this.setCheckedIndex(1);
        }

        return super.show();
    }

    public void setFatherViewText(String text){
        fatherView.setText(text);
    }
}
