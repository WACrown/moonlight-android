package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
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

public class SettingMenuModeSelectCreator extends SettingMenuCreator{

    public final static int MENU_MODE_SELECT_EDIT = 0;
    public final static int MENU_MODE_SELECT_MOVE = 1;
    public final static int MENU_MODE_SELECT_RESIZE = 2;
    public final static int MENU_MODE_SELECT_KEY_SELECT = 3;

    private final View settingMenu;
    private final List<View> settingMenuItemList = new ArrayList<>();
    private final Map<View, TextView> textViewOfSettingMenuItemViewMap = new HashMap<>();
    private final Context context;
    private final FrameLayout frameLayout;
    private final GameSetting gameSetting;


    public SettingMenuModeSelectCreator(Context context, FrameLayout frameLayout,GameSetting gameSetting) {

        this.context = context;
        this.frameLayout = frameLayout;
        this.gameSetting = gameSetting;
        settingMenu = LayoutInflater.from(context).inflate(R.layout.game_setting_menu_select_mode, null);
        settingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSetting.returnSettingMenu(null);
            }
        });
        settingMenu.setClickable(false);
        settingMenuItemList.add(settingMenu.findViewById(R.id.game_setting_menu_item_edit));
        settingMenuItemList.add(settingMenu.findViewById(R.id.game_setting_menu_item_move));
        settingMenuItemList.add(settingMenu.findViewById(R.id.game_setting_menu_item_resize));
        settingMenuItemList.add(settingMenu.findViewById(R.id.game_setting_menu_item_selectkey));
        textViewOfSettingMenuItemViewMap.put(settingMenuItemList.get(settingMenuItemList.size() - 1), settingMenu.findViewById(R.id.game_setting_menu_listview));

        settingMenu.setVisibility(View.INVISIBLE);
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int settingContainerHigh = (int)(screen.widthPixels);
        int settingContainerWidth = 400;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(settingContainerWidth, settingContainerHigh);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(settingMenu, params);
        bindOnClickListener();

    }

    public View getSettingMenu() {
        return settingMenu;
    }

    public List<View> getSettingMenuItemList() {
        return settingMenuItemList;
    }

    public Map<View, TextView> getTextViewOfSettingMenuItemViewMap() {
        return textViewOfSettingMenuItemViewMap;
    }

    public void bindOnClickListener(){

        settingMenuItemList.get(MENU_MODE_SELECT_EDIT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("game_setting_configuration_mode_edit");
            }
        });
        settingMenuItemList.get(MENU_MODE_SELECT_MOVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("game_setting_configuration_mode_edit");
            }
        });
        settingMenuItemList.get(MENU_MODE_SELECT_RESIZE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settingMenuItemList.get(MENU_MODE_SELECT_KEY_SELECT).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    gameSetting.displaySettingList(new ArrayList<>(),v);
                }
                return false;
            }
        });

    }

}
