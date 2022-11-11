package com.limelight.binding.input.virtual_controller.game_setting.item;

import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

import java.util.List;

public class MenuItemListSelect extends MenuItemPop{

    private final List<String> list;
    private final String preText;
    private final GameSetting gameSetting;
    private String dynamicText;

    public MenuItemListSelect(String name, GameSetting gameSetting, String text, List<String> list) {
        super(name, gameSetting);
        this.preText = text + " : ";
        this.list = list;
        this.gameSetting = gameSetting;
        this.setDynamicText(list.get(0));
    }

    @Override
    public void onClickPreAction() {
        gameSetting.displaySettingList(list,this);
    }


    public void setDynamicText(String dynamicText) {
        if (dynamicText != null){
            if (list.contains(dynamicText)){
                this.dynamicText = dynamicText;
                String fullText = preText + dynamicText;
                this.setText(fullText);
            } else {
                this.dynamicText = list.get(0);
                String fullText = preText + dynamicText;
                this.setText(fullText);
            }

        }
        runReturnAction();
    }

    public String getDynamicText(){
        return dynamicText;
    }

}
