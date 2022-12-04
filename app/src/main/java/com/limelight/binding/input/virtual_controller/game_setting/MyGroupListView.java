package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class MyGroupListView extends QMUIGroupListView {
    public MyGroupListView(Context context) {
        super(context);
    }

    public MyGroupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGroupListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static Section newSection(Context context) {
        return new MySection(context);
    }

    @Override
    public QMUICommonListItemView createItemView(@Nullable Drawable imageDrawable, CharSequence titleText, String detailText, int orientation, int accessoryType, int height) {
        QMUICommonListItemView itemView = new MyCommonListItemView(getContext());
        itemView.setOrientation(orientation);
        itemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        itemView.setImageDrawable(imageDrawable);
        itemView.setText(titleText);
        itemView.setDetailText(detailText);
        itemView.setAccessoryType(accessoryType);
        return itemView;
    }
}
