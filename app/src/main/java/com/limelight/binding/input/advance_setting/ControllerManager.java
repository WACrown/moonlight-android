package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.limelight.Game;
import com.limelight.R;

public class ControllerManager {

    private FrameLayout advanceSettingView;
    private FrameLayout fatherLayout;
    private ConfigUIController configController;
    private EditUIController editController;
    private PageSettingController settingController;
    private ElementController elementController;
    private TouchController touchController;
    private WindowsController windowsController;
    private PageCombineKeyController combineKeyController;
    private SimplifyPerformanceController simplifyPerformanceController;
    private KeyboardUIController keyboardController;
    private UIController openedUIController;
    private SuperPagesController superPagesController;
    private PageDeviceController pageDeviceController;
    private boolean isWindowOpen;
    private Context context;

    public ControllerManager(FrameLayout layout, Context context){
        advanceSettingView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.advance_setting_view,null);
        this.fatherLayout = layout;

        //window controller
        FrameLayout layerWindows = advanceSettingView.findViewById(R.id.layer_windows);
        windowsController = new WindowsController(this,layerWindows,context);

        FrameLayout layerElement = advanceSettingView.findViewById(R.id.layer_2_element);
        elementController = new ElementController(this,layerElement,context);

        View touchView = layerElement.findViewById(R.id.element_touch_view);
        touchController = new TouchController((Game) context,this,touchView);

        //Edit controller
        FrameLayout layerEdit = advanceSettingView.findViewById(R.id.layer_3_edit);
        editController = new EditUIController(this,layerEdit,context);

        //setting controller
        settingController = new PageSettingController(this, context);

        //configController
        FrameLayout layerConfig = advanceSettingView.findViewById(R.id.layer_5_config);
        configController = new ConfigUIController(this,layerConfig,context);

        FrameLayout layerKeyboard = advanceSettingView.findViewById(R.id.layer_6_keyboard);
        keyboardController = new KeyboardUIController(layerKeyboard,this,context);

        //CombineKey controller
        combineKeyController = new PageCombineKeyController(this,context);

        FrameLayout layerSimplifyPerformance = advanceSettingView.findViewById(R.id.layer_8_simplify_performance);
        simplifyPerformanceController = new SimplifyPerformanceController(layerSimplifyPerformance,this,context);

        FrameLayout superPagesBox = advanceSettingView.findViewById(R.id.super_pages_box);
        superPagesController = new SuperPagesController(superPagesBox,context);

        pageDeviceController = new PageDeviceController(context,this);


        configController.initLoadCurrentConfig();
    }


    public ConfigUIController getConfigController() {
        return configController;
    }

    public EditUIController getEditController() {
        return editController;
    }

    public PageSettingController getSettingController() {
        return settingController;
    }

    public ElementController getElementController() {
        return elementController;
    }

    public WindowsController getWindowsController() {
        return windowsController;
    }

    public TouchController getTouchController() {
        return touchController;
    }

    public PageCombineKeyController getCombineKeyController() {
        return combineKeyController;
    }

    public SimplifyPerformanceController getSimplifyPerformanceController() {
        return simplifyPerformanceController;
    }

    public KeyboardUIController getKeyboardController() {
        return keyboardController;
    }

    public boolean isWindowOpen() {
        return isWindowOpen;
    }

    public void setWindowOpen(boolean windowOpen) {
        isWindowOpen = windowOpen;
    }

    public UIController getOpenedController() {
        return openedUIController;
    }

    public void setOpenedController(UIController openedUIController) {
        this.openedUIController = openedUIController;
    }

    public SuperPagesController getSuperPagesController() {
        return superPagesController;
    }

    public PageDeviceController getDevicePageController() {
        return pageDeviceController;
    }

    public void refreshLayout(){
        fatherLayout.removeView(advanceSettingView);
        fatherLayout.addView(advanceSettingView);
    }

    private int dpToPx(int dp){
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}
