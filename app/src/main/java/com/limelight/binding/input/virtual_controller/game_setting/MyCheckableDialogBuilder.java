package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;

import com.limelight.R;
import com.limelight.utils.DBG;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MyCheckableDialogBuilder extends QMUIDialog.CheckableDialogBuilder {

    private QMUIRoundButton fatherView;
    private final ArrayList<CharSequence> keyList = new ArrayList<>();

    public MyCheckableDialogBuilder(Context context) {
        super(context);

    }

    @Override
    public QMUIDialog.CheckableDialogBuilder addItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
        keyList.addAll(Arrays.asList(items));
        return super.addItems(items, listener);
    }

    public QMUIDialog show(@NonNull QMUIRoundButton v) {
        fatherView = v;

        int index = keyList.indexOf(v.getText().toString());
        if (index != -1){
            this.setCheckedIndex(index);
        } else {
            this.setCheckedIndex(0);
        }

        return super.show();
    }

    public void setFatherViewText(String text){
        fatherView.setText(text);
    }
}
