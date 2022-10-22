package com.limelight.binding.input.virtual_controller.game_setting;

import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public abstract class SettingMenuCreator {

    public abstract View getSettingMenu();
    public abstract List<View> getSettingMenuItemList();
    public abstract Map<View, TextView> getTextViewOfSettingMenuItemViewMap();
}
