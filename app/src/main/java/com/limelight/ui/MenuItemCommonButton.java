package com.limelight.ui;

import android.content.Context;

import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

public class MenuItemCommonButton extends MenuItem{
    public MenuItemCommonButton(String name, GameSetting gameSetting, String text) {
        super(name, gameSetting.getContext());
        this.setText(text);
    }

    @Override
    public void onClickPreAction() {

    }
}
