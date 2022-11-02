package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;
import com.limelight.ui.AdapterSettingMenuListView;
import com.limelight.ui.MenuItemLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameSetting {

    private final Context context;
    private final FrameLayout frameLayout;
    private final View glassPanelEditor;
    private final Map<Integer, MenuItemLinearLayout> itemViewMap;
    private final SettingListCreator settingListCreator;
    private final Button buttonConfigure;
    private final View settingMenuLayout;
    private final int screenWidth;
    private final int screenHeight;
    private final VirtualController virtualController;


    private final List<List<MenuItemLinearLayout>> allMenu = new ArrayList<>();
    private final AdapterSettingMenuListView adapterSettingMenuListView;

    private MenuItemLinearLayout currentSelectedItem;
    private VirtualControllerElement editElement;


    public GameSetting(Context context, FrameLayout frameLayout, VirtualController virtualController){

        this.context = context;

        this.frameLayout = frameLayout;

        this.virtualController = virtualController;


        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        screenWidth = screen.widthPixels;
        screenHeight = screen.heightPixels;


        //透明编辑面板
        glassPanelEditor = new GlassEditorView(context);
        glassPanelEditor.setOnTouchListener(new View.OnTouchListener() {
            private boolean isMove = false;
            private boolean isOneFinger = false;
            private int startFingerPressedPositionX = 0;
            private int startFingerPressedPositionY = 0;
            private int startTwoFingerDistanceX = 0;
            private int startTwoFingerDistanceY = 0;
            private int startElementPositionX = 0;
            private int startElementPositionY = 0;
            private int startElementWidth = 0;
            private int startElementHeight = 0;
            private int maxElementPositionX = 0;
            private int maxElementPositionY = 0;
            private int maxElementWidth = 0;
            private int maxElementHeight = 0;
            private int elementCenterPositionX = 0;
            private int elementCenterPositionY = 0;

            private void setInitInfo(MotionEvent event){
                startFingerPressedPositionX = (int) event.getX();
                startFingerPressedPositionY = (int) event.getY();
                if (editElement != null){
                    startElementPositionX = (int) editElement.getX();
                    startElementPositionY = (int) editElement.getY();
                    startElementWidth = (int) editElement.getWidth();
                    startElementHeight = (int) editElement.getHeight();
                    maxElementPositionX = screenHeight - startElementWidth;
                    maxElementPositionY = screenWidth - startElementHeight;
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //System.out.println("wangguan panel touch:" + event.getAction());


                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        isMove = false;
                        isOneFinger = true;
                        setInitInfo(event);
                        return true;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        isOneFinger = false;
                        setInitInfo(event);
                        startTwoFingerDistanceX = Math.abs((int) event.getX(1) - (int) event.getX(0));
                        startTwoFingerDistanceY = Math.abs((int) event.getY(1) - (int) event.getY(0));
                        if (editElement != null){
                            elementCenterPositionX = startElementPositionX + startElementWidth/2;
                            elementCenterPositionY = startElementPositionY + startElementHeight/2;
                            if (screenHeight - elementCenterPositionX > elementCenterPositionX){
                                maxElementWidth = elementCenterPositionX*2;
                            } else {
                                maxElementWidth = (screenHeight - elementCenterPositionX)*2;
                            }

                            if (screenWidth - elementCenterPositionY > elementCenterPositionY){
                                maxElementHeight = elementCenterPositionY*2;
                            } else {
                                maxElementHeight = (screenWidth - elementCenterPositionY)*2;
                            }

                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        isMove = true;
                        if (editElement != null){
                            if (isOneFinger){
                                editElement.moveElement(maxElementPositionX,maxElementPositionY,startElementPositionX, startElementPositionY,(int) event.getX() - startFingerPressedPositionX,(int) event.getY() - startFingerPressedPositionY);
                            } else {
                                editElement.resizeElement(elementCenterPositionX,elementCenterPositionY,maxElementWidth,maxElementHeight,startElementWidth,startElementHeight,Math.abs(((int) event.getX(1) - (int) event.getX(0))) - startTwoFingerDistanceX,Math.abs(((int) event.getY(1) - (int) event.getY(0))) - startTwoFingerDistanceY);
                            }
                        }

                        return true;
                    case MotionEvent.ACTION_POINTER_UP:
                        isOneFinger = true;
                        setInitInfo(event);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!isMove) {
                            List<VirtualControllerElement> elements = virtualController.getElements();
                            for (int i = elements.size() - 1;i > -1;i--){
                                VirtualControllerElement element = elements.get(i);
                                int pressedX = (int) event.getX();
                                int pressedY = (int) event.getY();
                                int elementStartX = (int) element.getX();
                                int elementStartY = (int) element.getY();
                                int elementEndX = elementStartX + element.getWidth();
                                int elementEndY = elementStartY + element.getHeight();
                                if (((pressedX > elementStartX) && (pressedX < elementEndX)) && ((pressedY > elementStartY) && (pressedY < elementEndY))){

                                    //
                                    if (editElement != element && editElement != null) {
                                        editElement.setSelectedStatus(false);
                                    }
                                    if (element.getSelectedStatus()){
                                        editElement.setSelectedStatus(false);
                                        editElement = null;
                                    } else {
                                        editElement = element;
                                        editElement.setSelectedStatus(true);
                                    }

                                    break;


                                }
                            }
                        }
                        return true;
                }


                return true;
            }
        });

        //设置按钮
        buttonConfigure = new Button(context);
        buttonConfigure.setAlpha(0.25f);
        buttonConfigure.setFocusable(false);
        buttonConfigure.setBackgroundResource(R.drawable.ic_settings);
        buttonConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        SettingMenuItems settingMenuItems = new SettingMenuItems(context,this);
        itemViewMap = settingMenuItems.getItemViewMap();
        ListView menuListView = settingMenuLayout.findViewById(R.id.game_setting_menu_listview);
        adapterSettingMenuListView = new AdapterSettingMenuListView(context);
        menuListView.setAdapter(adapterSettingMenuListView);
        List<MenuItemLinearLayout> menu = new ArrayList<>();
        menu.add(null);
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_back_to_stream));
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_edit));
        refreshAdapterSettingMenuListViewList(menu,null);

        //列表选择副菜单
        settingListCreator = new SettingListCreator(context,frameLayout,this);
    }

    public Map<Integer, MenuItemLinearLayout> getItemViewMap() {
        return itemViewMap;
    }

    public List<List<MenuItemLinearLayout>> getAllMenu() {
        return allMenu;
    }



    public void back(){
        allMenu.get(allMenu.size() - 1).get(0).runCallback();
        allMenu.remove(allMenu.size() - 1);
    }

    public void editMode(boolean isEdit){
        if (isEdit){

            glassPanelEditor.setVisibility(View.VISIBLE);
        } else {
            glassPanelEditor.setVisibility(View.INVISIBLE);
            VirtualControllerConfigurationLoader.saveProfile(virtualController, context);
        }
    }


    public void addElement(String buttonNamePre,String elementSize){

        Set<String> allButtonName = new HashSet<>();

        for (VirtualControllerElement element : virtualController.getElements()){
            allButtonName.add(element.elementId);
        }

        for (int i = 0;i < 100;i ++){
            String buttonName = buttonNamePre + i;
            if (allButtonName.contains(buttonName)) {
                continue;
            }
            Map<String, String> newButton = new HashMap<>();
            newButton.put(buttonName,elementSize);
            VirtualControllerConfigurationLoader.createButtons(virtualController,context,newButton);
            //System.out.println("wangguan newButton:" + newButton);
            break;

        }
        Toast.makeText(context,"已添加",Toast.LENGTH_SHORT).show();


    }


    public void deleteElement(){
        if (editElement != null){
            frameLayout.removeView(editElement);
            virtualController.getElements().remove(editElement);
            editElement = null;
        }
    }

    public void setPanelVisibility(int visible){
        glassPanelEditor.setVisibility(visible);
    }

    public void setEditElement(VirtualControllerElement editElement) {
        this.editElement = editElement;
    }


    public void displaySettingList(List<String> currentSelectList, MenuItemLinearLayout currentSelectedItem){
        this.currentSelectedItem = currentSelectedItem;
        settingMenuLayout.setClickable(true); //上层layout拦截点击请求
        currentSelectedItem.setBackgroundColor(context.getResources().getColor(R.color.game_setting_item_background_color_pressed));
        settingListCreator.setSettingListContext(currentSelectList);
        settingListCreator.setSettingListVisibility(true);



    }

    public void returnSettingMenu(String selected){
        settingListCreator.setSettingListVisibility(false);
        if (selected != null){
            currentSelectedItem.setText(selected);
        }

        currentSelectedItem.setBackgroundColor(context.getResources().getColor(R.color.game_setting_item_background_color_primary));
        settingMenuLayout.setClickable(false);
        currentSelectedItem.runCallback();
    }



    public void refreshAdapterSettingMenuListViewList(List<MenuItemLinearLayout> menu,MenuItemLinearLayout fatherItem){
        if (menu != null){
            menu.add(0,fatherItem);
            allMenu.add(menu);
            adapterSettingMenuListView.setItemList(menu);
        } else {
            adapterSettingMenuListView.setItemList(allMenu.get(allMenu.size() - 1));
        }
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

        int buttonSize = (int)(screen.heightPixels*0.06f);
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
