package com.limelight.binding.input.virtual_controller.game_setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingKeyList extends SettingMenu{

    private final List<Integer> settingNameResourceIDList = new ArrayList<>(Arrays.asList(0,
            0, 0, 0));

    private final List<String> settingValueList = new ArrayList<>(Arrays.asList("RETURN", "RESIZE", "MOVE", "EDIT"));

    @Override
    public List<String> getSettingNameList() {
        return null;
    }

    @Override
    public List<String> getSettingValueList() {
        return null;
    }

}
