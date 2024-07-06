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
    private ConfigController configController;
    private EditController editController;
    private SettingController settingController;
    private ElementController elementController;
    private TouchController touchController;
    private WindowsController windowsController;
    private CombineKeyController combineKeyController;
    private SimplifyPerformanceController simplifyPerformanceController;
    private KeyboardController keyboardController;
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
        editController = new EditController(this,layerEdit,context);

        //setting controller
        FrameLayout layerSetting = advanceSettingView.findViewById(R.id.layer_4_setting);
        settingController = new SettingController(this,layerSetting,context);

        //configController
        FrameLayout layerConfig = advanceSettingView.findViewById(R.id.layer_5_config);
        configController = new ConfigController(this,layerConfig,context);

        FrameLayout layerKeyboard = advanceSettingView.findViewById(R.id.layer_6_keyboard);
        keyboardController = new KeyboardController(layerKeyboard,this,context);

        //CombineKey controller
        FrameLayout layerCombineKey = advanceSettingView.findViewById(R.id.layer_7_combine_key);
        combineKeyController = new CombineKeyController(layerCombineKey,this,context);

        FrameLayout layerSimplifyPerformance = advanceSettingView.findViewById(R.id.layer_8_simplify_performance);
        simplifyPerformanceController = new SimplifyPerformanceController(layerSimplifyPerformance,this,context);



        configController.initLoadCurrentConfig();
    }


    public ConfigController getConfigController() {
        return configController;
    }

    public EditController getEditController() {
        return editController;
    }

    public SettingController getSettingController() {
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

    public CombineKeyController getCombineKeyController() {
        return combineKeyController;
    }

    public SimplifyPerformanceController getSimplifyPerformanceController() {
        return simplifyPerformanceController;
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    public void refreshLayout(){
        fatherLayout.removeView(advanceSettingView);
        fatherLayout.addView(advanceSettingView);
    }

}
