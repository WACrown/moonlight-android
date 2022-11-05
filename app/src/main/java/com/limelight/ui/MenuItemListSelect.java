package com.limelight.ui;

import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

import java.util.List;

public class MenuItemListSelect extends MenuItem{

    private final List<String> list;
    private final String preText;
    private final GameSetting gameSetting;
    private String dynamicText;

    public MenuItemListSelect(String name, GameSetting gameSetting, String text, List<String> list) {
        super(name, gameSetting.getContext());
        this.preText = text + " : ";
        this.list = list;
        this.gameSetting = gameSetting;
        setDynamicText(0);
    }

    @Override
    public void onClickPreAction() {
        gameSetting.displaySettingList(list,this);
    }

    public String getDynamicText(){
        return dynamicText;
    }


    public void setDynamicText(int selectedIndex){
        this.dynamicText = list.get(selectedIndex);
        String fullText = preText + dynamicText;
        this.setText(fullText);
    }
}
