package com.limelight.binding.input.virtual_controller.game_setting.item;

import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

import java.util.List;

public class MenuItemFatherMenu extends MenuItemPop {


    private final List<MenuItem> menuContext;
    private final GameSetting gameSetting;

    public List<MenuItem> getMenuContext() {
        return menuContext;
    }

    public MenuItemFatherMenu(String name, GameSetting gameSetting, String text, List<MenuItem> menuContext) {
        super(name, gameSetting);
        this.menuContext = menuContext;
        this.gameSetting = gameSetting;
        this.setText(text);
    }

    @Override
    public void onClickPreAction() {
        gameSetting.goToNextMenu(this);
    }


}
