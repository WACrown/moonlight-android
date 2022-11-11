package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.ui.AdapterSettingMenuListView;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItem;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemFatherMenu;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemListSelect;


import java.util.ArrayList;

import java.util.List;

public class GameSetting {

    public Context getContext() {
        return context;
    }

    private final Context context;
    private final FrameLayout frameLayout;

    public View getGlassPanelEditor() {
        return glassPanelEditor;
    }

    private final View glassPanelEditor;//
    private final SettingListCreator settingListCreator;
    private final Button buttonConfigure;
    private final View settingMenuLayout;
    private final VirtualController virtualController;

    public List<MenuItemFatherMenu> getMenuQueue() {
        return menuQueue;
    }

    private final List<MenuItemFatherMenu> menuQueue = new ArrayList<>();
    private final AdapterSettingMenuListView adapterSettingMenuListView;

    public SettingMenuItemsController getSettingMenuItems() {
        return settingMenuItemsController;
    }

    private final SettingMenuItemsController settingMenuItemsController;

    private MenuItemListSelect currentSelectedItem;


    public GameSetting(Context context, FrameLayout frameLayout, VirtualController virtualController){

        this.context = context;
        this.frameLayout = frameLayout;
        this.virtualController = virtualController;




        //透明编辑面板
        glassPanelEditor = new View(context);


        //设置按钮
        buttonConfigure = new Button(context);
        buttonConfigure.setAlpha(0.25f);
        buttonConfigure.setFocusable(false);
        buttonConfigure.setBackgroundResource(R.drawable.ic_settings);
        buttonConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.VISIBLE);
            }
        });

        //列表选择副菜单
        settingListCreator = new SettingListCreator(context,frameLayout,this);

        //设置主菜单
        settingMenuLayout = LayoutInflater.from(context).inflate(R.layout.game_setting_menu_layout, null);
        settingMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSettingMenu(null);
            }
        });
        settingMenuLayout.setClickable(false);
        settingMenuLayout.setVisibility(View.INVISIBLE);
        ListView menuListView = settingMenuLayout.findViewById(R.id.game_setting_menu_listview);
        adapterSettingMenuListView = new AdapterSettingMenuListView(context);
        menuListView.setAdapter(adapterSettingMenuListView);
        settingMenuItemsController = new SettingMenuItemsController(context,this,virtualController);

    }



    public void editMode(boolean isEdit){
        if (isEdit){

            glassPanelEditor.setVisibility(View.VISIBLE);
        } else {
            glassPanelEditor.setVisibility(View.INVISIBLE);
        }
    }


    public void setPanelVisibility(int visible){
        glassPanelEditor.setVisibility(visible);
    }


    public void displaySettingList(List<String> currentSelectList, MenuItem currentSelectedItem){
        this.currentSelectedItem = (MenuItemListSelect) currentSelectedItem;
        settingMenuLayout.setClickable(true); //上层layout拦截点击请求
        settingListCreator.setSettingListContext(currentSelectList);
        settingListCreator.setSettingListVisibility(true);

    }

    public void returnSettingMenu(String selected){
        settingListCreator.setSettingListVisibility(false);
        currentSelectedItem.setDynamicText(selected);
        settingMenuLayout.setClickable(false);
    }

    public void goToNextMenu(MenuItemFatherMenu menu){
        menuQueue.add(menu);
        refreshMenu(menu.getMenuContext());
    }

    public void backToPreviousMenu(){
        MenuItemFatherMenu menu = menuQueue.remove(menuQueue.size() - 1);
        menu.runReturnAction();
        refreshMenu(menuQueue.get(menuQueue.size() - 1).getMenuContext());
    }



    public void refreshMenu(List<MenuItem> nextMenu){
        adapterSettingMenuListView.setItemList(nextMenu);
        adapterSettingMenuListView.notifyDataSetChanged();
    }


    public void setVisibility(int visible){

        if (settingMenuLayout != null){
            settingMenuLayout.setVisibility(visible);
        }

    }

    public void refreshLayout() {
        frameLayout.removeAllViews();
        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screen.widthPixels, screen.heightPixels);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(glassPanelEditor);
        glassPanelEditor.setVisibility(View.INVISIBLE);

        int buttonSize = (int)(screen.heightPixels*0.03f);
        params = new FrameLayout.LayoutParams(buttonSize, buttonSize);
        params.leftMargin = 15;
        params.topMargin = 15;
        frameLayout.addView(buttonConfigure, params);



        int settingContainerHigh = (int)(screen.widthPixels);
        int settingContainerWidth = 400;
        params = new FrameLayout.LayoutParams(settingContainerWidth, settingContainerHigh);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(settingMenuLayout, params);


        settingListCreator.refreshLayout();

    }

}
