package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;
import com.limelight.ui.SettingDialogBuilder;
import com.limelight.utils.PressedStartElementInfo;
import com.limelight.utils.controller.LayoutAdminHelper;
import com.limelight.utils.controller.LayoutList;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUISeekBar;
import com.qmuiteam.qmui.widget.QMUISlider;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameSetting {

    private final static int BUTTON_TYPE = 0;
    private final static int PAD_TYPE = 1;
    private final static int STICK_TYPE = 2;

    public Context getContext() {
        return context;
    }

    private final Context context;
    private final FrameLayout frameLayout;
    private final FrameLayout dialogFrame;
    private int screenWidth;
    private int screenHeight;
    private final View glassPanelEditor;
    private final Button buttonConfigure;
    private final VirtualController virtualController;
    private final QMUIGroupListView mGroupListView;
    private final View menuLayout;
    private final SettingDialogBuilder settingDialogBuilder;
    private ArrayList<View.OnClickListener> onClickListeners = new ArrayList<>();
    private final Map<VirtualControllerElement, PressedStartElementInfo> editElements = new HashMap<>();
    private MyCheckableDialogBuilder keySelect;
    private MyCheckableDialogBuilder layoutSelect;
    private MyCommonListItemView hideSettingItem;
    private MyCommonListItemView exitSettingItem;
    private MyCommonListItemView editModeItem;
    private MyCommonListItemView adjustOpacityItem;
    private MyCommonListItemView selectLayoutItem;
    private MyCommonListItemView addButtonItem;
    private MyCommonListItemView addPadItem;
    private MyCommonListItemView addStickItem;
    private MyCommonListItemView deleteElementItem;
    private MyCommonListItemView hideAllButtonItem;
    private MyCommonListItemView selectMouseModeItem;
    private MyCommonListItemView disableMouseItem;


    //private View testView;



    public GameSetting(Context context, FrameLayout frameLayout, VirtualController virtualController){

        this.context = context;
        this.frameLayout = frameLayout;
        this.virtualController = virtualController;
        this.menuLayout = LayoutInflater.from(context).inflate(R.layout.inner_menu_layout, null);
        this.mGroupListView = menuLayout.findViewById(R.id.groupListView);
        this.dialogFrame = new FrameLayout(context);
        this.settingDialogBuilder = new SettingDialogBuilder(context,dialogFrame);
        screenWidth = QMUIDisplayHelper.getScreenWidth(context);
        screenHeight = QMUIDisplayHelper.getScreenHeight(context);
//        testView = new View(context);
//        testView.setBackgroundColor(Color.RED);



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
        VirtualControllerConfigurationLoader.loadProfile(virtualController,context,mGroupListView);
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

    private void singleEditMode(VirtualControllerElement element){
        boolean previousStatus = element.getSelectedStatus();
        for (VirtualControllerElement editElement : editElements.keySet()){
            virtualController.setSelectedElement(editElement,false);
        }
        editElements.clear();
        if (!previousStatus){
            virtualController.setSelectedElement(element,true);
            editElements.put(element,new PressedStartElementInfo());
        }

    }

    private void multipleEditMode(VirtualControllerElement element){

        if (element.getSelectedStatus()){
            virtualController.setSelectedElement(element,false);
            editElements.remove(element);
        } else {
            virtualController.setSelectedElement(element,true);
            editElements.put(element,new PressedStartElementInfo());
        }

    }

    private void addElement(int elementType,View[] childViews){
        String buttonNamePre = "";
        switch (elementType) {

            case 0:
                buttonNamePre = "BUTTON-" +
                        ((QMUIRoundButton)childViews[0]).getText() + "-" +
                        ((QMUIRoundButton)childViews[1]).getText() + "-";

                break;
            case 1:
                buttonNamePre = "PAD-" +
                        ((QMUIRoundButton)childViews[0]).getText() + "-" +
                        ((QMUIRoundButton)childViews[1]).getText() + "-" +
                        ((QMUIRoundButton)childViews[2]).getText() + "-" +
                        ((QMUIRoundButton)childViews[3]).getText() + "-";

                break;
            case 2:
                buttonNamePre = "STICK-" +
                        ((QMUIRoundButton)childViews[0]).getText()+ "-" +
                        ((QMUIRoundButton)childViews[1]).getText() + "-" +
                        ((QMUIRoundButton)childViews[2]).getText() + "-" +
                        ((QMUIRoundButton)childViews[3]).getText() + "-" +
                        ((QMUIRoundButton)childViews[4]).getText() + "-";
                break;
        }

        Set<String> allButtonName = new HashSet<>();

        for (VirtualControllerElement element : virtualController.getElements()){
            allButtonName.add(element.getElementId());
        }

        for (int i = 0;i < 100;i ++){
            String buttonName = buttonNamePre + i;
            if (allButtonName.contains(buttonName)) {
                continue;
            }
            int buttonSize = (int) ((screenWidth - 40) * (((QMUISeekBar)childViews[childViews.length - 3]).getCurrentProgress() + 1) * 0.05);
            int left = frameLayout.getWidth()/2 - buttonSize/2;
            int top = frameLayout.getHeight()/2 - buttonSize/2;
            String buttonSizeString = "{\"LEFT\":" + left + ",\"TOP\":" + top + ",\"WIDTH\":" + buttonSize + ",\"HEIGHT\":" + buttonSize + "}";
            Map<String, String> newButton = new HashMap<>();
            newButton.put(buttonName,buttonSizeString);
            VirtualControllerConfigurationLoader.createButtons(virtualController,context,newButton);
            //System.out.println("wangguan newButton:" + newButton);
            break;

        }
        Toast.makeText(context,"已添加",Toast.LENGTH_SHORT).show();
    }





    private void initGroupListView(){

        final String[] keyItems = new String[]{"K-A", "K-B", "K-C", "K-D", "K-E", "K-F", "K-G", "K-H", "K-I", "K-J", "K-K", "K-L", "K-M", "K-N", "K-O", "K-P", "K-Q", "K-R", "K-S", "K-T", "K-U", "K-V", "K-W", "K-X", "K-Y", "K-Z",
                "K-ESC","K-CTRLL" , "K-SHIFTL", "K-CTRLR" , "K-SHIFTR", "K-ALTL"  , "K-ALTR"  , "K-ENTER" , "K-KBACK"  , "K-SPACE" , "K-TAB"   , "K-CAPS"  , "K-WIN", "K-DEL", "K-INS", "K-HOME", "K-END", "K-PGUP", "K-PGDN", "K-BREAK", "K-SLCK", "K-PRINT", "K-UP", "K-DOWN", "K-LEFT", "K-RIGHT",
                "K-1", "K-2", "K-3", "K-4", "K-5", "K-6", "K-7", "K-8", "K-9", "K-0", "K-F1", "K-F2", "K-F3", "K-F4", "K-F5", "K-F6", "K-F7", "K-F8", "K-F9", "K-F10", "K-F11", "K-F12",
                "K-~", "K-_", "K-=", "K-[", "K-]", "K-\\", "K-;", "\"", "K-<", "K->", "K-/",
                "K-NUM1", "K-NUM2", "K-NUM3", "K-NUM4", "K-NUM5", "K-NUM6", "K-NUM7", "K-NUM8", "K-NUM9", "K-NUM0", "K-NUM.", "K-NUM+", "K-NUM_", "K-NUM*", "K-NUM/", "K-NUMENT", "K-NUMLCK",
                "G-GA", "G-GB", "G-GX", "G-GY", "G-PU","G-PD","G-PL","G-PR","G-LT", "G-RT", "G-LB", "G-RB", "G-LSB", "G-RSB", "G-START","G-BACK","G-LSU","G-LSD","G-LSL","G-LSR","G-RSU","G-RSD","G-RSL","G-RSR",
                "M-ML", "M-MR", "M-MM", "M-M1", "M-M2"};
        keySelect = new MyCheckableDialogBuilder(context);
        keySelect.setCheckedIndex(1)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .addItems(keyItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        keySelect.setFatherViewText(keyItems[which]);
                    }
                })
                .create(R.style.QMUI_Dialog);

        LayoutList layoutList = LayoutAdminHelper.getLayoutList(context);
        final String[] layoutItems = layoutList.toArray(new String[layoutList.size()]);
        layoutSelect = new MyCheckableDialogBuilder(context);
        layoutSelect.setCheckedIndex(LayoutAdminHelper.getCurrentLayoutNum(context))
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .addItems(layoutItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        selectLayoutItem.setDetailText(layoutItems[which]);
                        LayoutAdminHelper.selectLayout(context,which);
                        VirtualControllerConfigurationLoader.loadProfile(virtualController,context,mGroupListView);
                    }
                })
                .create(R.style.QMUI_Dialog);


        glassPanelEditor.setOnTouchListener(new View.OnTouchListener() {
            private boolean isMove = false;
            private boolean isOneFinger = false;
            private int startFingerPressedPositionX = 0;
            private int startFingerPressedPositionY = 0;
            private int startTwoFingerDistanceX = 0;
            private int startTwoFingerDistanceY = 0;
            private int maxMoveLengthLeft;
            private int maxMoveLengthUp;
            private int maxMoveLengthRight;
            private int maxMoveLengthDown;
            private int maxIncreaseSizeX;
            private int maxIncreaseSizeY;

            private void setOneFingerStartInfo(MotionEvent event){
                screenWidth = frameLayout.getWidth();
                screenHeight = frameLayout.getHeight();
                maxMoveLengthLeft = screenWidth;
                maxMoveLengthUp = screenHeight;
                maxMoveLengthRight = screenWidth;
                maxMoveLengthDown = screenHeight;
                startFingerPressedPositionX = (int) event.getX();
                startFingerPressedPositionY = (int) event.getY();
                for (VirtualControllerElement editElement : editElements.keySet()){
                    PressedStartElementInfo startInfo = editElements.get(editElement);
                    startInfo.startElementPositionX = (int) editElement.getX();
                    startInfo.startElementPositionY = (int) editElement.getY();
                    startInfo.startElementWidth = (int) editElement.getWidth();
                    startInfo.startElementHeight = (int) editElement.getHeight();
                    maxMoveLengthLeft = Math.min(startInfo.startElementPositionX, maxMoveLengthLeft);
                    maxMoveLengthUp = Math.min(startInfo.startElementPositionY, maxMoveLengthUp);
                    maxMoveLengthRight = Math.min(screenWidth - (startInfo.startElementWidth + startInfo.startElementPositionX), maxMoveLengthRight);
                    maxMoveLengthDown = Math.min(screenHeight - (startInfo.startElementHeight + startInfo.startElementPositionY), maxMoveLengthDown);
                }

            }

            private void setTwoFingerStartInfo(MotionEvent event){
                startFingerPressedPositionX = (int) event.getX();
                startFingerPressedPositionY = (int) event.getY();
                startTwoFingerDistanceX = Math.abs((int) event.getX(1) - (int) event.getX(0));
                startTwoFingerDistanceY = Math.abs((int) event.getY(1) - (int) event.getY(0));
                maxIncreaseSizeX = screenWidth;
                maxIncreaseSizeY = screenHeight;
                for (VirtualControllerElement editElement : editElements.keySet()){
                    PressedStartElementInfo startInfo = editElements.get(editElement);
                    startInfo.startElementPositionX = (int) editElement.getX();
                    startInfo.startElementPositionY = (int) editElement.getY();
                    startInfo.startElementWidth = (int) editElement.getWidth();
                    startInfo.startElementHeight = (int) editElement.getHeight();
                    startInfo.elementCenterPositionX = startInfo.startElementPositionX + startInfo.startElementWidth/2;
                    startInfo.elementCenterPositionY = startInfo.startElementPositionY + startInfo.startElementHeight/2;
                    maxIncreaseSizeX = Math.min(startInfo.startElementPositionX,maxIncreaseSizeX);
                    maxIncreaseSizeX = Math.min(screenWidth - (startInfo.startElementPositionX + startInfo.startElementWidth),maxIncreaseSizeX);
                    maxIncreaseSizeY = Math.min(startInfo.startElementPositionY,maxIncreaseSizeY);
                    maxIncreaseSizeY = Math.min(screenHeight - (startInfo.startElementPositionY + startInfo.startElementHeight),maxIncreaseSizeY);
                }

            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //System.out.println("wangguan panel touch:" + event.getAction());


                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
//                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(20, 20);
//                        params.leftMargin = (int) event.getX();
//                        params.topMargin = (int) event.getY();
//                        testView.setLayoutParams(params);
//                        testView.setVisibility(View.VISIBLE);

                        isMove = false;
                        isOneFinger = true;
                        setOneFingerStartInfo(event);
                        return true;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        isOneFinger = false;
                        setTwoFingerStartInfo(event);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if ((Math.abs((int) event.getX() - startFingerPressedPositionX) > 3) || (Math.abs((int) event.getY() - startFingerPressedPositionY) > 3)){
                            isMove = true;
                            if (isOneFinger) {
                                virtualController.moveElements(editElements,
                                        (int) event.getX() - startFingerPressedPositionX,
                                        (int) event.getY() - startFingerPressedPositionY,
                                        maxMoveLengthLeft,
                                        maxMoveLengthUp,
                                        maxMoveLengthRight,
                                        maxMoveLengthDown);
                            } else {
                                virtualController.resizeElements(editElements,
                                        Math.abs(((int) event.getX(1) - (int) event.getX(0))) - startTwoFingerDistanceX,
                                        Math.abs(((int) event.getY(1) - (int) event.getY(0))) - startTwoFingerDistanceY,
                                        maxIncreaseSizeX,
                                        maxIncreaseSizeY);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_POINTER_UP:
                        isOneFinger = true;
                        setOneFingerStartInfo(event);
                        return true;
                    case MotionEvent.ACTION_UP:
//                        testView.setVisibility(View.INVISIBLE);
                        if (!isMove) {

                            List<VirtualControllerElement> elements = virtualController.getElements();
                            for (int i = elements.size() - 1; i > -1; i--) {
                                VirtualControllerElement element = elements.get(i);
                                int pressedX = (int) event.getX();
                                int pressedY = (int) event.getY();
                                int elementStartX = (int) element.getX();
                                int elementStartY = (int) element.getY();
                                int elementEndX = elementStartX + element.getWidth();
                                int elementEndY = elementStartY + element.getHeight();
                                if (((pressedX > elementStartX) && (pressedX < elementEndX)) && ((pressedY > elementStartY) && (pressedY < elementEndY))) {
                                    //System.out.println("wangguan moveElement:" + element.getElementId());
                                    if (true) {
                                        singleEditMode(element);
                                    } else {
                                        multipleEditMode(element);
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


        MyGroupListView.newSection(getContext())
                .setTitle("设置菜单")
                .addItemView(exitSettingItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(selectLayoutItem(),onClickListeners.get(onClickListeners.size() - 1))
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);

        MyGroupListView.newSection(getContext())
                .setTitle("按钮编辑菜单")
                .addItemView(hideAllButtonItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(adjustOpacityItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(editModeItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(hideSettingMenuItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(addButtonItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(addPadItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(addStickItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(deleteElementItem(), onClickListeners.get(onClickListeners.size() - 1))
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);

        MyGroupListView.newSection(getContext())
                .setTitle("鼠标编辑菜单")
                .addItemView(disableMouseItem(), onClickListeners.get(onClickListeners.size() - 1))
                .addItemView(selectMouseModeItem(), onClickListeners.get(onClickListeners.size() - 1))
                //.addItemView(adjustMouseMovableItem(), onClickListeners.get(onClickListeners.size() - 1))
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);


        hideSettingItem.setClickable(false);
        addButtonItem.setClickable(false);
        addStickItem.setClickable(false);
        addPadItem.setClickable(false);
        deleteElementItem.setClickable(false);

    }

    private QMUICommonListItemView exitSettingItem(){
        QMUICommonListItemView exitSettingItem = mGroupListView.createItemView("退出并保存");
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        this.exitSettingItem = (MyCommonListItemView) exitSettingItem;
        return exitSettingItem;
    }

    private QMUICommonListItemView hideSettingMenuItem(){
        QMUICommonListItemView hideSettingMenuItem = mGroupListView.createItemView("隐藏菜单");
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        hideSettingItem = (MyCommonListItemView) hideSettingMenuItem;
        return hideSettingMenuItem;
    }

    private QMUICommonListItemView selectLayoutItem(){
        QMUICommonListItemView selectLayoutItem = mGroupListView.createItemView("选择布局");
        selectLayoutItem.setOrientation(QMUICommonListItemView.VERTICAL);
        selectLayoutItem.setDetailText(LayoutAdminHelper.getCurrentLayoutName(context));
        selectLayoutItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               layoutSelect.show();
            }
        });

        this.selectLayoutItem = (MyCommonListItemView) selectLayoutItem;
        return selectLayoutItem;
    }

    private QMUICommonListItemView editModeItem(){
        QMUICommonListItemView editModeItem = mGroupListView.createItemView(
                null,
                "编辑模式",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        );
        editModeItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    virtualController.setCurrentMode(VirtualController.ControllerMode.EditButtons);
                    glassPanelEditor.setVisibility(View.VISIBLE);
                } else {
                    virtualController.setCurrentMode(VirtualController.ControllerMode.Active);
                    glassPanelEditor.setVisibility(View.INVISIBLE);
                }
                hideAllButtonItem.setClickable(!b);
                exitSettingItem.setClickable(!b);
                adjustOpacityItem.setClickable(!b);
                selectLayoutItem.setClickable(!b);
                hideSettingItem.setClickable(b);
                addButtonItem.setClickable(b);
                addStickItem.setClickable(b);
                addPadItem.setClickable(b);
                deleteElementItem.setClickable(b);

            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editModeItem.getSwitch().toggle();
            }
        });
        this.editModeItem = (MyCommonListItemView) editModeItem;
        return editModeItem;
    }

    private QMUICommonListItemView adjustOpacityItem(){
        //adjustOpacity
        QMUICommonListItemView adjustOpacityItem = mGroupListView.createItemView(
                null,
                "调整透明度",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );

        final View[] adjustOpacityChildViews = new View[3];
        //自定义dialog根view
        View adjustOpacityDialogRoot = settingDialogBuilder.createDialog(R.layout.top_slider);
        //dialog 黑色背景
        ((ViewGroup) adjustOpacityDialogRoot).getChildAt(0).setAlpha(0.7f);
        ((ViewGroup) adjustOpacityDialogRoot).getChildAt(0).setClickable(true);
        //dialog本体
        View dialog = ((ViewGroup) adjustOpacityDialogRoot).getChildAt(1);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) dialog.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 20;
        dialog.setLayoutParams(params);
        View innerLayout = ((ViewGroup)dialog).getChildAt(0);
        for (int i = 0;i < 3;i ++){
            adjustOpacityChildViews[i] = ((LinearLayout) innerLayout).getChildAt(i);
        }
        ((QMUISlider) adjustOpacityChildViews[1]).setCurrentProgress(100);
        ((TextView) adjustOpacityChildViews[0]).setText("100");
        virtualController.setOpacity(100);
        ((QMUISlider) adjustOpacityChildViews[1]).setCallback(new QMUISlider.Callback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                ((TextView) adjustOpacityChildViews[0]).setText("" + progress);
                virtualController.setOpacity(progress);
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
                adjustOpacityDialogRoot.setVisibility(View.INVISIBLE);
                menuLayout.setVisibility(View.VISIBLE);
            }
        });
        ((MyCommonListItemView) adjustOpacityItem).setChildViews(adjustOpacityChildViews);
        ((MyCommonListItemView) adjustOpacityItem).setStatusProcessor(new MyCommonListItemView.StatusProcessor() {
            @Override
            public void set(View[] childViews, String status) {
                String[] allConf = status.split(";");
                int progress = Integer.parseInt(allConf[0]);
                virtualController.setOpacity(progress);
                ((QMUISlider) childViews[1]).setCurrentProgress(progress);
                ((TextView) adjustOpacityChildViews[0]).setText("" + progress);
            }

            @Override
            public String get(View[] childViews) {
                return ((QMUISlider) childViews[1]).getCurrentProgress() + ";";
            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustOpacityDialogRoot.setVisibility(View.VISIBLE);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        this.adjustOpacityItem = (MyCommonListItemView) adjustOpacityItem;
        return adjustOpacityItem;
    }

    private QMUICommonListItemView addButtonItem(){
        QMUICommonListItemView addButtonItem = mGroupListView.createItemView(
                null,
                "新增按钮",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );

        View[] childViews = new View[5];
        View addButtonDialogRoot = settingDialogBuilder.createDialog(R.layout.add_button_dialog);
        View dialogBackground = ((ViewGroup) addButtonDialogRoot).getChildAt(0);
        dialogBackground.setClickable(true);
        ViewGroup dialog = (ViewGroup)((ViewGroup) addButtonDialogRoot).getChildAt(1);
        childViews[0] = dialog.findViewById(R.id.button_type);
        childViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[1] = dialog.findViewById(R.id.button_key);
        childViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[2] = dialog.findViewById(R.id.slider);
        ((QMUISeekBar)childViews[2]).setCurrentProgress(2);
        childViews[3] = dialog.findViewById(R.id.back);
        childViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButtonDialogRoot.setVisibility(View.INVISIBLE);
            }
        });

        childViews[4] = dialog.findViewById(R.id.add);
        childViews[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElement(BUTTON_TYPE,childViews);
                addButtonDialogRoot.setVisibility(View.INVISIBLE);
            }
        });

        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addButtonDialogRoot.setVisibility(View.VISIBLE);
            }
        });
        this.addButtonItem = (MyCommonListItemView) addButtonItem;
        return addButtonItem;
    }

    private QMUICommonListItemView addPadItem(){
        QMUICommonListItemView addPadItem = mGroupListView.createItemView(
                null,
                "新增十字键",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );
        //addPadDialogBuilder
        View[] childViews = new View[7];
        View addPadDialogRoot = settingDialogBuilder.createDialog(R.layout.add_pad_dialog);
        View dialogBackground = ((ViewGroup) addPadDialogRoot).getChildAt(0);
        dialogBackground.setClickable(true);
        ViewGroup dialog = (ViewGroup)((ViewGroup) addPadDialogRoot).getChildAt(1);
        childViews[0] = dialog.findViewById(R.id.pad_up);
        childViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[1] = dialog.findViewById(R.id.pad_down);
        childViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[2] = dialog.findViewById(R.id.pad_left);
        childViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[3] = dialog.findViewById(R.id.pad_right);
        childViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[4] = dialog.findViewById(R.id.pad_size);
        ((QMUISeekBar)childViews[4]).setCurrentProgress(2);
        childViews[5] = dialog.findViewById(R.id.back);
        childViews[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPadDialogRoot.setVisibility(View.INVISIBLE);
            }
        });

        childViews[6] = dialog.findViewById(R.id.add);
        childViews[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElement(PAD_TYPE,childViews);
                addPadDialogRoot.setVisibility(View.INVISIBLE);
            }
        });

        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPadDialogRoot.setVisibility(View.VISIBLE);
            }
        });
        this.addPadItem = (MyCommonListItemView) addPadItem;
        return addPadItem;
    }

    private QMUICommonListItemView addStickItem(){
        QMUICommonListItemView addStickItem = mGroupListView.createItemView(
                null,
                "新增摇杆",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        );
        //addPadDialogBuilder
        View[] childViews = new View[8];
        View addStickDialogRoot = settingDialogBuilder.createDialog(R.layout.add_stick_dialog);
        View dialogBackground = ((ViewGroup) addStickDialogRoot).getChildAt(0);
        dialogBackground.setClickable(true);
        ViewGroup dialog = (ViewGroup)((ViewGroup) addStickDialogRoot).getChildAt(1);
        childViews[0] = dialog.findViewById(R.id.stick_up);
        childViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[1] = dialog.findViewById(R.id.stick_down);
        childViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[2] = dialog.findViewById(R.id.stick_left);
        childViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[3] = dialog.findViewById(R.id.stick_right);
        childViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[4] = dialog.findViewById(R.id.stick_middle);
        childViews[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelect.show((QMUIRoundButton) v);
            }
        });
        childViews[5] = dialog.findViewById(R.id.stick_size);
        ((QMUISeekBar)childViews[5]).setCurrentProgress(2);
        childViews[6] = dialog.findViewById(R.id.back);
        childViews[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStickDialogRoot.setVisibility(View.INVISIBLE);
            }
        });

        childViews[7] = dialog.findViewById(R.id.add);
        childViews[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElement(STICK_TYPE,childViews);
                addStickDialogRoot.setVisibility(View.INVISIBLE);
            }
        });
        ((MyCommonListItemView) addStickItem).setChildViews(childViews);
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStickDialogRoot.setVisibility(View.VISIBLE);
            }
        });
        this.addStickItem = (MyCommonListItemView) addStickItem;
        return addStickItem;
    }

    private QMUICommonListItemView deleteElementItem(){
        QMUICommonListItemView deleteElementItem = mGroupListView.createItemView("删除按键");
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                virtualController.removeElements(editElements.keySet());
                editElements.clear();
            }
        });
        this.deleteElementItem = (MyCommonListItemView) deleteElementItem;
        return deleteElementItem;
    }

    private QMUICommonListItemView hideAllButtonItem(){
        QMUICommonListItemView hideAllButtonItem = mGroupListView.createItemView(
                null,
                "隐藏所有按键",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        );
        hideAllButtonItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                virtualController.hideButton(b);
                adjustOpacityItem.setClickable(!b);
                editModeItem.setClickable(!b);

            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllButtonItem.getSwitch().toggle();
            }
        });
        this.hideAllButtonItem = (MyCommonListItemView) hideAllButtonItem;
        return hideAllButtonItem;
    }

    private QMUICommonListItemView disableMouseItem(){
        QMUICommonListItemView disableMouseItem = mGroupListView.createItemView(
                null,
                "禁用鼠标",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        );
        disableMouseItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                virtualController.disableMouse(b);

            }
        });

        ((MyCommonListItemView) disableMouseItem).setStatusProcessor(new MyCommonListItemView.StatusProcessor() {
            @Override
            public void set(View[] childViews, String status) {
                String[] statuses = status.split(";");
                boolean isChecked = Boolean.parseBoolean(statuses[0]);
                disableMouseItem.getSwitch().setChecked(isChecked);
                virtualController.disableMouse(isChecked);
            }

            @Override
            public String get(View[] childViews) {
                return disableMouseItem.getSwitch().isChecked() + "";
            }
        });

        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableMouseItem.getSwitch().toggle();
            }
        });
        this.disableMouseItem = (MyCommonListItemView) disableMouseItem;
        return disableMouseItem;
    }

    private QMUICommonListItemView selectMouseModeItem(){
        QMUICommonListItemView selectMouseModeItem = mGroupListView.createItemView(
                null,
                "切换鼠标模式",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        );
        selectMouseModeItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                virtualController.isTouchMode(b);
            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMouseModeItem.getSwitch().toggle();
            }
        });
        ((MyCommonListItemView)selectMouseModeItem).setStatusProcessor(new MyCommonListItemView.StatusProcessor() {
            @Override
            public void set(View[] childViews, String status) {
                String[] statuses = status.split(";");
                boolean isChecked = Boolean.parseBoolean(statuses[0]);
                selectMouseModeItem.getSwitch().setChecked(isChecked);
                virtualController.isTouchMode(isChecked);
            }

            @Override
            public String get(View[] childViews) {
                return selectMouseModeItem.getSwitch().isChecked() + "";
            }
        });

        this.selectMouseModeItem = (MyCommonListItemView) selectMouseModeItem;
        return selectMouseModeItem;
    }

    private QMUICommonListItemView adjustMouseMovableItem(){
        QMUICommonListItemView adjustMouseMovableItem = mGroupListView.createItemView("调整鼠标速度");
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return adjustMouseMovableItem;
    }


    public void refreshLayout() {
        frameLayout.removeAllViews();
        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(glassPanelEditor);
        glassPanelEditor.setVisibility(View.INVISIBLE);

//        params = new FrameLayout.LayoutParams(20, 20);
//        params.leftMargin = 0;
//        params.topMargin = 0;
//        frameLayout.addView(testView);
//        testView.setVisibility(View.INVISIBLE);

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

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = 0;
        params.topMargin = 0;
        frameLayout.addView(dialogFrame,params);

    }

}
