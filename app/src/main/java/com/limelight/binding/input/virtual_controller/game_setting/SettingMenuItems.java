package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.ui.MenuItemLinearLayout;
import com.limelight.ui.MenuLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingMenuItems {

    private final Map<Integer, MenuItemLinearLayout> itemViewMap = new HashMap<>();
    private final GameSetting gameSetting;


    public SettingMenuItems(Context context, FrameLayout frameLayout,GameSetting gameSetting) {

        this.gameSetting = gameSetting;
        MenuLinearLayout settingMenu = (MenuLinearLayout) LayoutInflater.from(context).inflate(R.layout.game_setting_menu_items, null);
        for (int i = 0;i < settingMenu.getChildCount();i ++){
            MenuItemLinearLayout menuItemLinearLayout = (MenuItemLinearLayout) settingMenu.getChildAt(i);
            itemViewMap.put(menuItemLinearLayout.getId(),menuItemLinearLayout);
            if (menuItemLinearLayout.getChildCount() == 3){
                menuItemLinearLayout.setTextView((TextView) menuItemLinearLayout.getChildAt(2));
            }
        }
        bindOnClickListener();
    }

    public Map<Integer, MenuItemLinearLayout> getItemViewMap() {
        return itemViewMap;
    }


    public void bindOnClickListener(){

        itemViewMap.get(R.id.game_setting_menu_item_back_to_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSetting.setVisibility(View.INVISIBLE);
            }
        });


        itemViewMap.get(R.id.game_setting_menu_item_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<List<MenuItemLinearLayout>> allMenu = gameSetting.getAllMenu();
                allMenu.remove(allMenu.size() - 1);
                gameSetting.refreshAdapterSettingMenuListViewList();
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_edit).setMenu(Arrays.asList(
                itemViewMap.get(R.id.game_setting_menu_item_back_to_stream),
                itemViewMap.get(R.id.game_setting_menu_item_back),
                itemViewMap.get(R.id.game_setting_menu_item_edit_add),
                itemViewMap.get(R.id.game_setting_menu_item_edit_delete),
                itemViewMap.get(R.id.game_setting_menu_item_edit_move),
                itemViewMap.get(R.id.game_setting_menu_item_edit_resize)
        ),gameSetting);

        itemViewMap.get(R.id.game_setting_menu_item_edit_add).setMenu(Arrays.asList(
                itemViewMap.get(R.id.game_setting_menu_item_back_to_stream),
                itemViewMap.get(R.id.game_setting_menu_item_back),
                itemViewMap.get(R.id.game_setting_menu_item_edit_add_button),
                itemViewMap.get(R.id.game_setting_menu_item_edit_add_pad),
                itemViewMap.get(R.id.game_setting_menu_item_edit_add_stick)
        ),gameSetting);




        itemViewMap.get(R.id.game_setting_menu_item_selectkey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(gameSetting.keyList,(MenuItemLinearLayout) v);
            }
        });


    }
}
