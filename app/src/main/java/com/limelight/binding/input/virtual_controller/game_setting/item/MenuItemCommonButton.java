package com.limelight.binding.input.virtual_controller.game_setting.item;

import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

public class MenuItemCommonButton extends MenuItemButton{
    public MenuItemCommonButton(String name, GameSetting gameSetting, String text) {
        super(name, gameSetting);
        this.setText(text);
    }


}
