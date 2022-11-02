package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.ui.MenuItemLinearLayout;
import com.limelight.ui.MenuLinearLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingMenuItems {

    public final List<String> keyList = Arrays.asList("K-A", "K-B", "K-C", "K-D", "K-E", "K-F", "K-G", "K-H", "K-I", "K-J", "K-K", "K-L", "K-M", "K-N", "K-O", "K-P", "K-Q", "K-R", "K-S", "K-T", "K-U", "K-V", "K-W", "K-X", "K-Y", "K-Z",
            "K-ESC","K-CTRLL" , "K-SHIFTL", "K-CTRLR" , "K-SHIFTR", "K-ALTL"  , "K-ALTR"  , "K-ENTER" , "K-KBACK"  , "K-SPACE" , "K-TAB"   , "K-CAPS"  , "K-WIN", "K-DEL", "K-INS", "K-HOME", "K-END", "K-PGUP", "K-PGDN", "K-BREAK", "K-SLCK", "K-PRINT", "K-UP", "K-DOWN", "K-LEFT", "K-RIGHT",
            "K-1", "K-2", "K-3", "K-4", "K-5", "K-6", "K-7", "K-8", "K-9", "K-0", "K-F1", "K-F2", "K-F3", "K-F4", "K-F5", "K-F6", "K-F7", "K-F8", "K-F9", "K-F10", "K-F11", "K-F12",
            "K-~", "K-_", "K-=", "K-[", "K-]", "K-\\", "K-;", "\"", "K-<", "K->", "K-/",
            "K-NUM1", "K-NUM2", "K-NUM3", "K-NUM4", "K-NUM5", "K-NUM6", "K-NUM7", "K-NUM8", "K-NUM9", "K-NUM0", "K-NUM.", "K-NUM+", "K-NUM_", "K-NUM*", "K-NUM/", "K-NUMENT", "K-NUMLCK",
            "G-GA", "G-GB", "G-GX", "G-GY", "G-PU","G-PD","G-PL","G-PR","G-LT", "G-RT", "G-LB", "G-RB", "G-LSB", "G-RSB", "G-START","G-BACK","G-LSU","G-LSD","G-LSL","G-LSR","G-RSU","G-RSD","G-RSL","G-RSR",
            "M-ML", "M-MR", "M-MM", "M-M1", "M-M2");

    private final List<String> funcList = Arrays.asList("COMMON");
    private final List<String> elementSize = Arrays.asList("small","middle","big");
    private final Map<String,String> elementSizeMap;


    private final Map<Integer, MenuItemLinearLayout> itemViewMap = new HashMap<>();
    private final GameSetting gameSetting;


    public SettingMenuItems(Context context, GameSetting gameSetting) {

        this.elementSizeMap = new HashMap<>();
        elementSizeMap.put("small","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        elementSizeMap.put("middle","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        elementSizeMap.put("big","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");

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


        itemViewMap.get(R.id.game_setting_menu_item_edit).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {

            List<MenuItemLinearLayout> menu = Arrays.asList(
                    itemViewMap.get(R.id.game_setting_menu_item_back_to_stream),
                    itemViewMap.get(R.id.game_setting_menu_item_back),
                    itemViewMap.get(R.id.game_setting_menu_item_hide_menu),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_add_button),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_add_pad),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_add_stick),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_delete)
            );

            @Override
            public void callback() {
                gameSetting.editMode(false);
            }

            @Override
            public void onClick(View v) {
                gameSetting.editMode(true);
                gameSetting.refreshAdapterSettingMenuListViewList(menu,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_edit_add_button).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {

            List<MenuItemLinearLayout> menu = Arrays.asList(
                    itemViewMap.get(R.id.game_setting_menu_item_back_to_stream),
                    itemViewMap.get(R.id.game_setting_menu_item_back),
                    itemViewMap.get(R.id.game_setting_menu_item_hide_menu),
                    itemViewMap.get(R.id.game_setting_menu_item_select_button),
                    itemViewMap.get(R.id.game_setting_menu_item_select_button_type),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_add)
            );

            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {

            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_edit_add_pad).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {

            List<MenuItemLinearLayout> menu = Arrays.asList(
                    itemViewMap.get(R.id.game_setting_menu_item_back_to_stream),
                    itemViewMap.get(R.id.game_setting_menu_item_back),
                    itemViewMap.get(R.id.game_setting_menu_item_hide_menu),
                    itemViewMap.get(R.id.game_setting_menu_item_select_up),
                    itemViewMap.get(R.id.game_setting_menu_item_select_down),
                    itemViewMap.get(R.id.game_setting_menu_item_select_right),
                    itemViewMap.get(R.id.game_setting_menu_item_select_left),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_add)
            );

            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {

            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_edit_add_stick).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {

            List<MenuItemLinearLayout> menu = Arrays.asList(
                    itemViewMap.get(R.id.game_setting_menu_item_back_to_stream),
                    itemViewMap.get(R.id.game_setting_menu_item_back),
                    itemViewMap.get(R.id.game_setting_menu_item_hide_menu),
                    itemViewMap.get(R.id.game_setting_menu_item_select_button),
                    itemViewMap.get(R.id.game_setting_menu_item_select_up),
                    itemViewMap.get(R.id.game_setting_menu_item_select_down),
                    itemViewMap.get(R.id.game_setting_menu_item_select_right),
                    itemViewMap.get(R.id.game_setting_menu_item_select_left),
                    itemViewMap.get(R.id.game_setting_menu_item_edit_add)
            );

            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {

            }
        });


        itemViewMap.get(R.id.game_setting_menu_item_back_to_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSetting.setVisibility(View.INVISIBLE);
                for (int i = gameSetting.getAllMenu().size() - 1;i == 0;i --){
                    gameSetting.back();
                }
                gameSetting.refreshAdapterSettingMenuListViewList(null,null);
            }
        });


        itemViewMap.get(R.id.game_setting_menu_item_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSetting.back();
                gameSetting.refreshAdapterSettingMenuListViewList(null,null);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_hide_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSetting.setVisibility(View.INVISIBLE);
            }
        });


        itemViewMap.get(R.id.game_setting_menu_item_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wangguan delete");
                gameSetting.deleteElement();
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_edit_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wangguan add");
                String buttonNamePre = "";
                List<List<MenuItemLinearLayout>> allMenu = gameSetting.getAllMenu();
                switch (allMenu.get(allMenu.size() - 1).get(0).getId()) {

                    case R.id.game_setting_menu_item_edit_add_button:
                        buttonNamePre = "BUTTON-" + itemViewMap.get(R.id.game_setting_menu_item_select_button_type).getText() + "-"+ itemViewMap.get(R.id.game_setting_menu_item_select_button).getText() + "-";

                        break;
                    case R.id.game_setting_menu_item_edit_add_pad:
                        buttonNamePre = "PAD-" + itemViewMap.get(R.id.game_setting_menu_item_select_up).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_down).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_right).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_left).getText() + "-";

                        break;
                    case R.id.game_setting_menu_item_edit_add_stick:
                        buttonNamePre = "STICK-" + itemViewMap.get(R.id.game_setting_menu_item_select_up).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_down).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_right).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_left).getText() + "-" + itemViewMap.get(R.id.game_setting_menu_item_select_button).getText() + "-";
                        break;
                }

                gameSetting.addElement(buttonNamePre,elementSizeMap.get(itemViewMap.get(R.id.game_setting_menu_item_select_button_size).getText()));

            }

        });

        itemViewMap.get(R.id.game_setting_menu_item_select_button).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(keyList,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_select_up).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(keyList,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_select_down).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(keyList,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_select_left).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(keyList,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_select_right).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(keyList,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_select_button_type).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(funcList,(MenuItemLinearLayout) v);
            }
        });

        itemViewMap.get(R.id.game_setting_menu_item_select_button_size).setOnClickListener(new MenuItemLinearLayout.OnClickAndBackListener() {
            @Override
            public void callback() {

            }

            @Override
            public void onClick(View v) {
                gameSetting.displaySettingList(elementSize,(MenuItemLinearLayout) v);
            }
        });

    }
}
