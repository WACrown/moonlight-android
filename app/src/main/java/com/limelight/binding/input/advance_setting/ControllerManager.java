package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.binding.input.advance_setting.combinekey.PageCombineKeyController;
import com.limelight.binding.input.advance_setting.config.PageConfigController;
import com.limelight.binding.input.advance_setting.sqlite.SuperConfigDatabaseHelper;
import com.limelight.binding.input.advance_setting.superpage.SuperPagesController;

public class ControllerManager {

    private FrameLayout advanceSettingView;
    private FrameLayout fatherLayout;
    private PageConfigController configController;
    private TouchController touchController;
    private PageCombineKeyController combineKeyController;
    private SuperPagesController superPagesController;
    private PageDeviceController pageDeviceController;
    private SuperConfigDatabaseHelper superConfigDatabaseHelper;
    private Context context;

    public ControllerManager(FrameLayout layout, Context context){
        advanceSettingView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.advance_setting_view,null);
        this.fatherLayout = layout;

        superConfigDatabaseHelper = new SuperConfigDatabaseHelper(context);

        FrameLayout layerElement = advanceSettingView.findViewById(R.id.layer_2_element);
        touchController = new TouchController((Game) context,this,layerElement);

        //configController
        configController = new PageConfigController(this,context);

        //CombineKey controller
        combineKeyController = new PageCombineKeyController(this,context);

        FrameLayout superPagesBox = advanceSettingView.findViewById(R.id.super_pages_box);
        superPagesController = new SuperPagesController(superPagesBox,context);

        pageDeviceController = new PageDeviceController(context,this);

        configController.initConfig();

    }


    public PageConfigController getConfigController() {
        return configController;
    }


    public TouchController getTouchController() {
        return touchController;
    }

    public PageCombineKeyController getCombineKeyController() {
        return combineKeyController;
    }


    public SuperPagesController getSuperPagesController() {
        return superPagesController;
    }

    public PageDeviceController getDevicePageController() {
        return pageDeviceController;
    }

    public SuperConfigDatabaseHelper getSuperConfigDatabaseHelper() {
        return superConfigDatabaseHelper;
    }

    public void refreshLayout(){
        fatherLayout.removeView(advanceSettingView);
        fatherLayout.addView(advanceSettingView);
    }


}
