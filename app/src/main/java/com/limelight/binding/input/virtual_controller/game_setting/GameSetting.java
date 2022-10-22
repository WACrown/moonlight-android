package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.ui.AdapterSettingMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSetting {


    public final List<String> keyList = Arrays.asList("K-A", "K-B", "K-C", "K-D", "K-E", "K-F", "K-G", "K-H", "K-I", "K-J", "K-K", "K-L", "K-M", "K-N", "K-O", "K-P", "K-Q", "K-R", "K-S", "K-T", "K-U", "K-V", "K-W", "K-X", "K-Y", "K-Z",
            "K-ESC","K-CTRLL" , "K-SHIFTL", "K-CTRLR" , "K-SHIFTR", "K-ALTL"  , "K-ALTR"  , "K-ENTER" , "K-KBACK"  , "K-SPACE" , "K-TAB"   , "K-CAPS"  , "K-WIN", "K-DEL", "K-INS", "K-HOME", "K-END", "K-PGUP", "K-PGDN", "K-BREAK", "K-SLCK", "K-PRINT", "K-UP", "K-DOWN", "K-LEFT", "K-RIGHT",
            "K-1", "K-2", "K-3", "K-4", "K-5", "K-6", "K-7", "K-8", "K-9", "K-0", "K-F1", "K-F2", "K-F3", "K-F4", "K-F5", "K-F6", "K-F7", "K-F8", "K-F9", "K-F10", "K-F11", "K-F12",
            "K-~", "K-_", "K-=", "K-[", "K-]", "K-\\", "K-;", "\"", "K-<", "K->", "K-/",
            "K-NUM1", "K-NUM2", "K-NUM3", "K-NUM4", "K-NUM5", "K-NUM6", "K-NUM7", "K-NUM8", "K-NUM9", "K-NUM0", "K-NUM.", "K-NUM+", "K-NUM_", "K-NUM*", "K-NUM/", "K-NUMENT", "K-NUMLCK",
            "G-GA", "G-GB", "G-GX", "G-GY", "G-PU","G-PD","G-PL","G-PR","G-LT", "G-RT", "G-LB", "G-RB", "G-LSB", "G-RSB", "G-START","G-BACK","G-LSU","G-LSD","G-LSL","G-LSR","G-RSU","G-RSD","G-RSL","G-RSR",
            "M-ML", "M-MR", "M-MM", "M-M1", "M-M2");

    private final List<String> typeList = Arrays.asList("BUTTON", "PAD", "STICK");
    private final List<String> funcList = Arrays.asList("COMMON");




    private final Context context;
    private final SettingListCreator settingListCreator;

    private final View settingMenuLayout;
    private final Map<Integer, View> itemViewMap;
    private final Map<View, TextView> textViewOfItem;
    private final List<List<View>> allMenu = new ArrayList<>();
    private final AdapterSettingMenuListView adapterSettingMenuListView;

    private View currentSelectedItem;


    public GameSetting(Context context, FrameLayout frameLayout){

        this.context = context;
        settingListCreator = new SettingListCreator(context,frameLayout,this);
        SettingMenuItems settingMenuItems = new SettingMenuItems(context, frameLayout, this);
        itemViewMap = settingMenuItems.getItemViewMap();
        textViewOfItem = settingMenuItems.getTextViewOfItem();
        settingMenuLayout = LayoutInflater.from(context).inflate(R.layout.game_setting_menu_layout, null);
        settingMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSettingMenu(null);
            }
        });
        settingMenuLayout.setClickable(false);
        ListView menuListView = settingMenuLayout.findViewById(R.id.game_setting_menu_listview);
        adapterSettingMenuListView = new AdapterSettingMenuListView(context);
        menuListView.setAdapter(adapterSettingMenuListView);
        settingMenuLayout.setVisibility(View.INVISIBLE);
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int settingContainerHigh = (int)(screen.widthPixels);
        int settingContainerWidth = 400;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(settingContainerWidth, settingContainerHigh);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(settingMenuLayout, params);

        List<View> menu = new ArrayList<>();
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_edit));
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_selectkey));

        allMenu.add(menu);
        refreshAdapterSettingMenuListViewList();
    }

    public Map<Integer, View> getItemViewMap() {
        return itemViewMap;
    }

    public List<List<View>> getAllMenu() {
        return allMenu;
    }

    public void refreshAdapterSettingMenuListViewList(){
        adapterSettingMenuListView.setItemList(allMenu.get(allMenu.size() - 1));
        adapterSettingMenuListView.notifyDataSetChanged();
    }


    public void setVisibility(int visible){

        if (settingMenuLayout != null){
            settingMenuLayout.setVisibility(visible);
        }

    }



    public void displaySettingList(List<String> currentSelectList,View currentSelectedItem){
        this.currentSelectedItem = currentSelectedItem;
        settingMenuLayout.setClickable(true); //上层layout拦截点击请求
        currentSelectedItem.setBackgroundColor(context.getResources().getColor(R.color.game_setting_item_background_color_pressed));
        settingListCreator.setSettingListContext(currentSelectList);
        settingListCreator.setSettingListVisibility(true);



    }

    public void returnSettingMenu(String selected){
        settingListCreator.setSettingListVisibility(false);
        if (selected != null){
            textViewOfItem.get(currentSelectedItem).setText(selected);
        }
        currentSelectedItem.setBackgroundColor(context.getResources().getColor(R.color.game_setting_item_background_color_primary));
        currentSelectedItem = null;
        settingMenuLayout.setClickable(false);
    }


}
