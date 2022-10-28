package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;
import com.limelight.ui.AdapterSettingMenuListView;
import com.limelight.ui.MenuItemLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final FrameLayout frameLayout;
    private final View glassPanelEditor;
    private final Map<Integer, MenuItemLinearLayout> itemViewMap;
    private final SettingListCreator settingListCreator;
    private final Button buttonConfigure;
    private final View settingMenuLayout;

    private final List<List<MenuItemLinearLayout>> allMenu = new ArrayList<>();
    private final AdapterSettingMenuListView adapterSettingMenuListView;

    private MenuItemLinearLayout currentSelectedItem;
    private VirtualControllerElement editElement;
    private List<VirtualControllerElement> elements;


    public GameSetting(Context context, FrameLayout frameLayout, VirtualController virtualController){

        this.context = context;
        this.frameLayout = frameLayout;



        //透明编辑面板
        glassPanelEditor = new GlassEditorView(context);
        glassPanelEditor.setOnTouchListener(new View.OnTouchListener() {
            private boolean isMove = false;
            private boolean isOneFinger = false;
            private int pressedX = 0;
            private int pressedY = 0;
            private int distanceX = 0;
            private int distanceY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                System.out.println("wangguan panel touch:" + event.getAction());


                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        isMove = false;
                        isOneFinger = true;
                        pressedX = (int) event.getX();
                        pressedY = (int) event.getY();
                        return true;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (editElement != null){
                            isOneFinger = false;
                            distanceX = (int) event.getX(1) - (int) event.getX(0);
                            distanceY = (int) event.getY(1) - (int) event.getY(0);
                            editElement.setStartSize();
                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        isMove = true;
                        if (editElement != null){
                            if (isOneFinger){
                                editElement.moveElement(pressedX, pressedY,(int) event.getX(),(int) event.getY());
                            } else {
                                editElement.resizeElement(distanceX,distanceY,(int) event.getX(1) - (int) event.getX(0),(int) event.getY(1) - (int) event.getY(0));
                            }
                        }

                        return true;
                    case MotionEvent.ACTION_POINTER_UP:
                        isOneFinger = true;
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
                                    if (editElement != element && editElement != null) {
                                        editElement.switchSelectedStatus();
                                    }
                                    if (element.switchSelectedStatus())
                                        editElement = element;
                                    else
                                        editElement = null;
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
        SettingMenuItems settingMenuItems = new SettingMenuItems(context, frameLayout, this);
        itemViewMap = settingMenuItems.getItemViewMap();
        ListView menuListView = settingMenuLayout.findViewById(R.id.game_setting_menu_listview);
        adapterSettingMenuListView = new AdapterSettingMenuListView(context);
        menuListView.setAdapter(adapterSettingMenuListView);
        List<MenuItemLinearLayout> menu = new ArrayList<>();
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_back_to_stream));
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_edit));
        menu.add(itemViewMap.get(R.id.game_setting_menu_item_selectkey));
        allMenu.add(menu);
        refreshAdapterSettingMenuListViewList();

        //列表选择副菜单
        settingListCreator = new SettingListCreator(context,frameLayout,this);
    }

    public Map<Integer, MenuItemLinearLayout> getItemViewMap() {
        return itemViewMap;
    }

    public List<List<MenuItemLinearLayout>> getAllMenu() {
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

    public void setPanelVisibility(int visible){
        glassPanelEditor.setVisibility(visible);
    }

    public void setEditElement(VirtualControllerElement editElement) {
        this.editElement = editElement;
    }

    public VirtualControllerElement getEditElement() {
        return editElement;
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
        currentSelectedItem = null;
        settingMenuLayout.setClickable(false);
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
        //frameLayout.addView(buttonConfigure, params);



        int settingContainerHigh = (int)(screen.widthPixels);
        int settingContainerWidth = 400;
        params = new FrameLayout.LayoutParams(settingContainerWidth, settingContainerHigh);
        params.leftMargin = 0;
        params.topMargin = 0;
        //frameLayout.addView(settingMenuLayout, params);


        //settingListCreator.refreshLayout();

    }

}
