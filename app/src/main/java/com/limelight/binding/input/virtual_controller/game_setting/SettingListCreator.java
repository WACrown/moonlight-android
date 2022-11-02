package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.ui.AdapterSettingListListView;

import java.util.List;

public class SettingListCreator {

    private final ListView settingList;
    private final AdapterSettingListListView adapterSettingListListView;
    private final Context context;
    private final GameSetting gameSetting;
    private final FrameLayout frameLayout;


    public SettingListCreator(Context context, FrameLayout frameLayout, GameSetting gameSetting) {

        this.context = context;
        this.frameLayout = frameLayout;
        this.gameSetting = gameSetting;
        settingList = new ListView(context);
        settingList.setBackgroundColor(context.getResources().getColor(R.color.game_setting_list_background_color_primary));
        adapterSettingListListView = new AdapterSettingListListView(context);
        settingList.setAdapter(adapterSettingListListView);

        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedKey = (String) parent.getItemAtPosition(position);
                gameSetting.returnSettingMenu(selectedKey);
            }
        });

        settingList.setVisibility(View.INVISIBLE);


    }

    public void setSettingListContext(List<String> currentSelectList){
        adapterSettingListListView.setItemList(currentSelectList);
        adapterSettingListListView.notifyDataSetChanged();
    }

    public void setSettingListVisibility(boolean display){
        if (display){
            settingList.setVisibility(View.VISIBLE);
        } else {
            settingList.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshLayout() {
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int listContainerHigh = (int)(screen.widthPixels);
        int listContainerWidth = 300;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(listContainerWidth, listContainerHigh);
        params.leftMargin = 400;
        params.topMargin = 0;
        frameLayout.addView(settingList, params);
    }

}
