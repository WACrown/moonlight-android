package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.limelight.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingMenuItems {

    private final Map<Integer, View> itemViewMap = new HashMap<>();
    private final Map<View, TextView> textViewOfItem = new HashMap<>();
    private final GameSetting gameSetting;
    private final View settingMenu;

    public SettingMenuItems(Context context, FrameLayout frameLayout,GameSetting gameSetting) {

        this.gameSetting = gameSetting;
        settingMenu = LayoutInflater.from(context).inflate(R.layout.game_setting_menu_select_mode, null);

        int resId = 0;
        resId = R.id.game_setting_menu_item_edit;
        itemViewMap.put(resId,settingMenu.findViewById(resId));
        resId = R.id.game_setting_menu_item_resize;
        itemViewMap.put(resId,settingMenu.findViewById(resId));
        resId = R.id.game_setting_menu_item_move;
        itemViewMap.put(resId,settingMenu.findViewById(resId));
        resId = R.id.game_setting_menu_item_selectkey;
        View selectorItem = settingMenu.findViewById(resId);
        itemViewMap.put(resId,selectorItem);
        textViewOfItem.put(selectorItem,settingMenu.findViewById(R.id.game_setting_menu_item_selectkey_textview));
        resId = R.id.game_setting_menu_back;
        itemViewMap.put(resId,settingMenu.findViewById(resId));


        bindOnClickListener();
    }

    public View getSettingMenu() {
        return settingMenu;
    }

    public Map<Integer, View> getItemViewMap() {
        return itemViewMap;
    }

    public Map<View, TextView> getTextViewOfItem() {
        return textViewOfItem;
    }

    public void bindOnClickListener(){

        itemViewMap.get(R.id.game_setting_menu_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back");
                List<List<View>> allMenu = gameSetting.getAllMenu();
                allMenu.remove(allMenu.size() - 1);
                gameSetting.refreshAdapterSettingMenuListViewList();
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("game_setting_configuration_mode_edit");
                List<View> menu = new ArrayList<>();
                menu.add(itemViewMap.get(R.id.game_setting_menu_back));
                menu.add(itemViewMap.get(R.id.game_setting_menu_item_resize));
                gameSetting.getAllMenu().add(menu);
                gameSetting.refreshAdapterSettingMenuListViewList();
            }
        });
        itemViewMap.get(R.id.game_setting_menu_item_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("game_setting_configuration_mode_edit");
            }
        });
        itemViewMap.get(R.id.game_setting_menu_item_resize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        itemViewMap.get(R.id.game_setting_menu_item_selectkey).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    gameSetting.displaySettingList(gameSetting.keyList,v);
                }
                return false;
            }
        });

    }
}
