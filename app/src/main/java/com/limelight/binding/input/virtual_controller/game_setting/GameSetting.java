package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUISeekBar;
import com.qmuiteam.qmui.widget.QMUISlider;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;


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

    private final View glassPanelEditor;
    private final Button buttonConfigure;
    private final VirtualController virtualController;
    private final QMUIGroupListView mGroupListView;
    private final View menuLayout;

    //-----------------------------
    private MyCustomDialogBuilder addPadDialogBuilder;
    private MyCustomDialogBuilder addStickDialogBuilder;
    private MyCustomDialogBuilder addButtonDialogBuilder;
    private MyCheckableDialogBuilder keySelect;
    private MyCustomDialogBuilder adjustMouseMovableDialogBuilder;
    private MyCustomDialogBuilder adjustOpacityDialogBuilder;

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
                show();
            }
        });

        initGroupListView();

    }

    public void show(){
        menuLayout.setVisibility(View.VISIBLE);
        buttonConfigure.setVisibility(View.INVISIBLE);
    }

    public void dismiss(){
        menuLayout.setVisibility(View.INVISIBLE);
        buttonConfigure.setVisibility(View.VISIBLE);
    }

    private void exit(){
        menuLayout.setVisibility(View.INVISIBLE);
        VirtualControllerConfigurationLoader.saveProfile(virtualController,context,mGroupListView);
        buttonConfigure.setVisibility(View.VISIBLE);
    }


    private void initGroupListView(){

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


        MyGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .addItemView(exitSettingItem(), exitSettingItemListener())
                .addItemView(hideSettingMenuItem(), hideSettingMenuItemListener())
                .addItemView(selectLayoutItem(),selectLayoutItemListener())
                .addItemView(editModeItem(), editModeItemListener())
                .addItemView(adjustOpacityItem(), adjustOpacityItemListener())
                .addItemView(addButtonItem(), addButtonItemListener())
                .addItemView(addPadItem(), addPadItemListener())
                .addItemView(addStickItem(), addStickItemListener())
                .addItemView(deleteElementItem(), deleteElementItemListener())
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);

    }

    private QMUICommonListItemView exitSettingItem(){
        QMUICommonListItemView exitSettingItem = mGroupListView.createItemView("Exit");
        return exitSettingItem;
    }

    private View.OnClickListener exitSettingItemListener(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        };
    }

    private QMUICommonListItemView hideSettingMenuItem(){
        QMUICommonListItemView hideSettingMenuItem = mGroupListView.createItemView("Hide Menu");
        return hideSettingMenuItem;
    }

    private View.OnClickListener hideSettingMenuItemListener(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    private QMUICommonListItemView selectLayoutItem(){
        QMUICommonListItemView selectLayoutItem = mGroupListView.createItemView("Select Layout");
        selectLayoutItem.setOrientation(QMUICommonListItemView.VERTICAL);
        selectLayoutItem.setDetailText("星露谷");
        selectLayoutItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        return selectLayoutItem;
    }

    private View.OnClickListener selectLayoutItemListener(){

        return null;
    }

    private QMUICommonListItemView editModeItem(){
        QMUICommonListItemView editModeItem = mGroupListView.createItemView(
                null,
                "Edit Mode",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        );
        return editModeItem;
    }

    private View.OnClickListener editModeItemListener(){

        return null;
    }

    private QMUICommonListItemView adjustOpacityItem(){
        //adjustOpacity
        QMUICommonListItemView adjustOpacityItem = mGroupListView.createItemView(
                null,
                "Adjust Opacity",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );

        final View[] adjustOpacityChildViews = new View[3];
        adjustOpacityDialogBuilder = new MyCustomDialogBuilder(context);
        adjustOpacityDialogBuilder.setLayout(R.layout.top_slider);
        adjustOpacityDialogBuilder.setLayoutParamSetter(new MyCustomDialogBuilder.layoutParamSetter() {
            @Override
            public void operation(View layout,QMUIDialog dialog) {
                for (int i = 0;i < 3;i ++){
                    adjustOpacityChildViews[i] = ((LinearLayout) layout).getChildAt(i);
                }
                ((QMUISlider) adjustOpacityChildViews[1]).setCallback(new QMUISlider.Callback() {
                    @Override
                    public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                        ((TextView) adjustOpacityChildViews[0]).setText("" + progress);
                    }

                    @Override
                    public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {

                    }

                    @Override
                    public void onTouchUp(QMUISlider slider, int progress, int tickCount) {

                    }

                    @Override
                    public void onStartMoving(QMUISlider slider, int progress, int tickCount) {

                    }

                    @Override
                    public void onStopMoving(QMUISlider slider, int progress, int tickCount) {

                    }
                });

                adjustOpacityChildViews[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
        adjustOpacityDialogBuilder.setDimAmount(0f);
        adjustOpacityDialogBuilder.setPosition(MyCustomDialogBuilder.HORIZONTAL_CENTER,20);
        adjustOpacityDialogBuilder.setCanceledOnTouchOutside(false);
        adjustOpacityDialogBuilder.create(R.style.QMUI_Dialog);
        ((MyCommonListItemView) adjustOpacityItem).setChildViews(adjustOpacityChildViews);
        ((MyCommonListItemView) adjustOpacityItem).setStatusProcessor(new MyCommonListItemView.StatusProcessor() {
            @Override
            public void set(View[] childViews, String status) {
                String[] allConf = status.split(";");
                int progress = Integer.parseInt(allConf[0]);
                ((QMUISlider) childViews[1]).setCurrentProgress(progress);
                ((TextView) adjustOpacityChildViews[0]).setText("" + progress);
            }

            @Override
            public String get(View[] childViews) {
                return ((QMUISlider) childViews[1]).getCurrentProgress() + ";";
            }
        });
        return adjustOpacityItem;
    }

    private View.OnClickListener adjustOpacityItemListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustOpacityDialogBuilder.show();
            }
        };
    }

    private QMUICommonListItemView addButtonItem(){
        QMUICommonListItemView addButtonItem = mGroupListView.createItemView(
                null,
                "Add Button",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );
        return addButtonItem;
    }

    private View.OnClickListener addButtonItemListener(){
        return null;
    }

    private QMUICommonListItemView addStickItem(){
        QMUICommonListItemView addStickItem = mGroupListView.createItemView(
                null,
                "Add Stick",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );
        //addPadDialogBuilder
        final View[] childViews = new View[6];
        addStickDialogBuilder = new MyCustomDialogBuilder(context);
        addStickDialogBuilder.setLayout(R.layout.add_stick_dialog);
        addStickDialogBuilder.setLayoutParamSetter(new MyCustomDialogBuilder.layoutParamSetter() {
            @Override
            public void operation(View layout,QMUIDialog dialog) {
                childViews[0] = layout.findViewById(R.id.stick_up);
                childViews[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[1] = layout.findViewById(R.id.stick_down);
                childViews[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[2] = layout.findViewById(R.id.stick_left);
                childViews[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[3] = layout.findViewById(R.id.stick_right);
                childViews[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[4] = layout.findViewById(R.id.stick_middle);
                childViews[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[5] = layout.findViewById(R.id.stick_size);
            }
        });
        addStickDialogBuilder.addAction("添加", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                System.out.println("wangguan up:" + ((QMUIRoundButton)childViews[0]).getText()
                        + "\ndown:" + ((QMUIRoundButton)childViews[1]).getText()
                        + "\nleft:" + ((QMUIRoundButton)childViews[2]).getText()
                        + "\nright" + ((QMUIRoundButton)childViews[3]).getText()
                        + "\nsize:" + ((QMUISeekBar)childViews[5]).getCurrentProgress());
                dialog.dismiss();
            }
        });
        addStickDialogBuilder.addAction("返回", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        addStickDialogBuilder.setCanceledOnTouchOutside(false);
        addStickDialogBuilder.create(R.style.QMUI_Dialog);
        ((MyCommonListItemView) addStickItem).setChildViews(childViews);
        return addStickItem;
    }

    private View.OnClickListener addStickItemListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStickDialogBuilder.show();
            }
        };
    }

    private QMUICommonListItemView addPadItem(){
        QMUICommonListItemView addPadItem = mGroupListView.createItemView(
                null,
                "Add Pad",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );
        //addPadDialogBuilder
        final View[] childViews = new View[5];
        addPadDialogBuilder = new MyCustomDialogBuilder(context);
        addPadDialogBuilder.setLayout(R.layout.add_pad_dialog);
        addPadDialogBuilder.setLayoutParamSetter(new MyCustomDialogBuilder.layoutParamSetter() {
            @Override
            public void operation(View layout,QMUIDialog dialog) {
                childViews[0] = layout.findViewById(R.id.pad_up);
                childViews[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[1] = layout.findViewById(R.id.pad_down);
                childViews[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[2] = layout.findViewById(R.id.pad_left);
                childViews[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[3] = layout.findViewById(R.id.pad_right);
                childViews[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keySelect.show((QMUIRoundButton) v);
                    }
                });
                childViews[4] = layout.findViewById(R.id.pad_size);
            }
        });
        addPadDialogBuilder.addAction("添加", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                System.out.println("wangguan up:" + ((QMUIRoundButton)childViews[0]).getText()
                        + "\ndown:" + ((QMUIRoundButton)childViews[1]).getText()
                        + "\nleft:" + ((QMUIRoundButton)childViews[2]).getText()
                        + "\nright" + ((QMUIRoundButton)childViews[3]).getText()
                        + "\nsize:" + ((QMUISeekBar)childViews[4]).getCurrentProgress());
                dialog.dismiss();
            }
        });
        addPadDialogBuilder.addAction("返回", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        addPadDialogBuilder.setCanceledOnTouchOutside(false);
        addPadDialogBuilder.create(R.style.QMUI_Dialog);
        return addPadItem;
    }

    private View.OnClickListener addPadItemListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPadDialogBuilder.show();
            }
        };
    }

    private QMUICommonListItemView deleteElementItem(){
        QMUICommonListItemView deleteElementItem = mGroupListView.createItemView("Delete Element");
        return deleteElementItem;
    }

    private View.OnClickListener deleteElementItemListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyCommonListItemView> myCommonListItemViews = ((MySection)mGroupListView.getSection(0)).getMyCommonListItemViews();
                System.out.println("wangguan " + myCommonListItemViews.size());
            }
        };
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


        int settingContainer2High = (int)(screen.widthPixels);
        int settingContainer2Width = 600;
        params = new FrameLayout.LayoutParams(settingContainer2Width, settingContainer2High);
        params.leftMargin = 0;
        params.topMargin = 0;
        menuLayout.setVisibility(View.INVISIBLE);
        frameLayout.addView(menuLayout, params);

    }

}
