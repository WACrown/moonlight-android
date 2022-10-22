package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingMenu {

    public abstract List<String> getSettingNameList();

    public abstract List<String> getSettingValueList();


    protected List<String> getNameTextList(Context context,List<Integer> settingNameResourceIDList,List<String> settingValueList){
        List<String> settingNameList = new ArrayList<>();

        if (settingNameResourceIDList.size() < settingValueList.size()){
            for (int i = settingNameResourceIDList.size();i < settingValueList.size();i ++){
                settingNameResourceIDList.add(0);
            }
        } else if (settingNameResourceIDList.size() > settingValueList.size()){
            for (int i = settingNameResourceIDList.size();i > settingValueList.size();i --){
                settingNameResourceIDList.remove(i - 1);
            }
        }

        for (int i = 0;i < settingNameResourceIDList.size();i++){
            int resourceId = settingNameResourceIDList.get(i);

            if (resourceId == 0) {
                settingNameList.add(settingValueList.get(i));
            } else {
                settingNameList.add(context.getResources().getString(resourceId));
            }

        }

        return settingNameList;
    }
}
