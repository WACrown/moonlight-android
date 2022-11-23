package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.game_setting.item.MyCheckableDialogBuilder;
import com.limelight.ui.AdapterSettingMenuListView;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItem;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemFatherMenu;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemListSelect;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUISeekBar;
import com.qmuiteam.qmui.widget.QMUISlider;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;


import java.util.ArrayList;

import java.util.Collections;
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

    private final QMUIGroupListView mGroupListView;
    private final View menuLayout;

    //-----------------------------
    private MyCustomDialogBuilder addPadDialogBuilder;
    private MyCheckableDialogBuilder keySelect;

    private QMUIPopup keyPopup;



    public GameSetting(Context context, FrameLayout frameLayout, VirtualController virtualController){

        this.context = context;
        this.frameLayout = frameLayout;
        this.virtualController = virtualController;
        this.menuLayout = LayoutInflater.from(context).inflate(R.layout.inner_menu_layout, null);
        this.mGroupListView = menuLayout.findViewById(R.id.groupListView);




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
        initPopup();
        initDialog();
        initGroupListView();


    }

    private void initGroupListView(){
        QMUICommonListItemView exitSettingItem = mGroupListView.createItemView("Exit");

        QMUICommonListItemView hideSettingMenuItem = mGroupListView.createItemView("Hide Menu");

        QMUICommonListItemView selectLayoutItem = mGroupListView.createItemView(
                null,
                "Select Layout",
                "layout",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView editModeItem = mGroupListView.createItemView(
                null,
                "Edit Mode",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
                );

        QMUICommonListItemView adjustOpacityItem = mGroupListView.createItemView(
                null,
                "Adjust Opacity",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );


        QMUICommonListItemView addButtonItem = mGroupListView.createItemView(
                null,
                "Add Button",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );

        QMUICommonListItemView addPadItem = mGroupListView.createItemView(
                null,
                "Add Pad",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );

        QMUICommonListItemView addStickItem = mGroupListView.createItemView(
                null,
                "Add Stick",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );

        QMUICommonListItemView deleteElementItem = mGroupListView.createItemView("Delete Element");


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(context, text + " is Clicked", Toast.LENGTH_SHORT).show();
                    if (((QMUICommonListItemView) v).getAccessoryType() == QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((QMUICommonListItemView) v).getSwitch().toggle();
                    }
                }
            }
        };


        View.OnClickListener addPadOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addPadDialogBuilder.show();
            }
        };

        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .addItemView(exitSettingItem, onClickListener)
                .addItemView(hideSettingMenuItem, onClickListener)
                .addItemView(editModeItem, onClickListener)
                .addItemView(adjustOpacityItem, null)
                .addItemView(addButtonItem, onClickListener)
                .addItemView(addPadItem, addPadOnclickListener)
                .addItemView(addStickItem, onClickListener)
                .addItemView(deleteElementItem, onClickListener)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);



    }

    private void initDialog(){
        addPadDialogBuilder = new MyCustomDialogBuilder(context);
        addPadDialogBuilder.setLayout(R.layout.add_pad_dialog);
        addPadDialogBuilder.setLayoutParamSetter(new MyCustomDialogBuilder.layoutParamSetter() {
            @Override
            public void operation(View layout) {
                layout.findViewById(R.id.pad_up).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                layout.findViewById(R.id.pad_down).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keyPopup.show(v);
                    }
                });
            }
        });
        addPadDialogBuilder.addAction("添加",null);
        addPadDialogBuilder.addAction("返回",null);
        addPadDialogBuilder.create(R.style.QMUI_Dialog);


        final String[] items = new String[]{"选项1", "选项2", "选项3", "选项4", "选项5", "选项6", "选项7", "选项8", "选项9", "选项10", "选项11"};
        final int checkedIndex = 1;
        keySelect = new MyCheckableDialogBuilder(context);
                keySelect.setCheckedIndex(checkedIndex)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        keySelect.setFatherViewText(items[which]);
                    }
                })
                .create(R.style.QMUI_Dialog);

    }


    private void initPopup(){
        String[] listItems = new String[]{
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.popup_textview, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Item " + (i + 1), Toast.LENGTH_SHORT).show();
                if (keyPopup != null) {
                    keyPopup.dismiss();
                }
            }
        };
        keyPopup = QMUIPopups.listPopup(getContext(),
                        QMUIDisplayHelper.dp2px(getContext(), 250),
                        QMUIDisplayHelper.dp2px(getContext(), 300),
                        adapter,
                        onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_LEFT)
                .preferredDirection(QMUIPopup.DIRECTION_CENTER_IN_SCREEN)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5))
                .skinManager(QMUISkinManager.defaultInstance(getContext()))
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(getContext(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                });


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

        int settingContainer2High = (int)(screen.widthPixels);
        int settingContainer2Width = 600;
        params = new FrameLayout.LayoutParams(settingContainer2Width, settingContainer2High);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(menuLayout, params);




        settingListCreator.refreshLayout();

    }

}
