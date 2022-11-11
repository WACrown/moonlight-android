package com.limelight.binding.input.virtual_controller.game_setting.item;

import android.view.View;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

public abstract class MenuItemButton extends MenuItem{

    private OnMenuItemClickListener onMenuItemClickListener;

    public MenuItemButton(String name, GameSetting gameSetting) {
        super(name, gameSetting.getContext());
        this.setBackground(gameSetting.getContext().getDrawable(R.drawable.game_setting_bg_selector));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuItemClickListener.onClick(v);
            }
        });
    }

    @Override
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        onMenuItemClickListener.onCreate(this);
    }


}
