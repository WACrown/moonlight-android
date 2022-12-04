package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;

public class MySection extends QMUIGroupListView.Section {

    public ArrayList<MyCommonListItemView> getMyCommonListItemViews() {
        return myCommonListItemViews;
    }

    private final ArrayList<MyCommonListItemView> myCommonListItemViews = new ArrayList<>();
    public MySection(Context context) {
        super(context);
    }

    @Override
    public QMUIGroupListView.Section addItemView(QMUICommonListItemView itemView, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        myCommonListItemViews.add((MyCommonListItemView) itemView);
        return super.addItemView(itemView, onClickListener, onLongClickListener);
    }
}
