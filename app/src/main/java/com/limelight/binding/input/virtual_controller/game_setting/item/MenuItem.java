package com.limelight.binding.input.virtual_controller.game_setting.item;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limelight.R;

public abstract class MenuItem extends TextView {


    public interface OnMenuItemClickListener{
        void onCreate(View v);
        void onClick(View v);
        void returnAction(View v);
    }

    public String getName() {
        return name;
    }

    private final String name;

    public MenuItem(String name,Context context) {
        super(context);
        this.name = name;
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100));
        this.setTextColor(context.getResources().getColor(R.color.game_setting_item_text_color));
        this.setTextSize(15);
        this.setPadding(4,0,0,0);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setBackgroundColor(context.getResources().getColor(R.color.game_setting_item_background_color_primary));
    }

    public abstract void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener);


}
