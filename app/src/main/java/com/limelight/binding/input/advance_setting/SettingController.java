package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.binding.video.PerformanceInfo;
import com.limelight.preferences.PreferenceConfiguration;

import java.util.Map;

public class SettingController extends Controller{

    private static final String MOUSE_SENSE = "mouse_sense";
    private static final String ELEMENT_OPACITY = "element_opacity";
    private static final String MOUSE_ENABLE = "mouse_enable";
    private static final String MOUSE_MODE = "mouse_mode";
    private static final String SIMPLIFY_PERFORMANCE = "simplify_performance";
    private static final String SIMPLIFY_PERFORMANCE_OPACITY = "simplify_performance_opacity";

    private SettingPreference settingPreference;
    private ControllerManager controllerManager;
    private FrameLayout settingLayout;
    //simplifyPerformance


    private TextView msenseTextView;
    private SeekBar elementOpacitySeekbar;
    private Switch mouseEnableSwitch;
    private Switch mouseModeSwitch;
    private Switch simplifyPerformanceSwitch;
    private SeekBar simplifyPerformanceSeekBar;


    private Context context;

    public SettingController(ControllerManager controllerManager, FrameLayout settingLayout, Context context){
        this.controllerManager = controllerManager;
        this.settingLayout = settingLayout;
        this.context = context;
        msenseTextView = settingLayout.findViewById(R.id.msense_textview);
        elementOpacitySeekbar = settingLayout.findViewById(R.id.element_opacity_seekbar);
        mouseEnableSwitch = settingLayout.findViewById(R.id.mouse_enable_switch);
        mouseModeSwitch = settingLayout.findViewById(R.id.trackpad_enable_switch);
        simplifyPerformanceSwitch = settingLayout.findViewById(R.id.simplify_performance_display);
        simplifyPerformanceSeekBar = settingLayout.findViewById(R.id.simplify_performance_opacity_seekbar);

        initMouseSense();
        initElementOpacity();
        initMouseEnable();
        initMouseMode();
        initSimplifyPerformance();
    }


    private void initMouseSense(){
        int min = 1;
        int max = 500;
        WindowsController.EditTextWindowListener inputMsenseListener = new WindowsController.EditTextWindowListener() {
            @Override
            public boolean onConfirmClick(String text) {
                if (text.equals("")){
                    Toast.makeText(context,"请输入" + min + "~" + max + "的数字",Toast.LENGTH_SHORT).show();
                    return false;
                }
                int value = Integer.parseInt(text);
                if (value > max || value < min){
                    Toast.makeText(context,"请输入" + min + "~" + max + "的数字",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String sense = String.valueOf(value);
                msenseTextView.setText(sense);
                doSetting(MOUSE_SENSE, sense);
                settingPreference.saveSetting(MOUSE_SENSE, sense);

                return true;
            }

            @Override
            public boolean onCancelClick(String text) {
                return true;
            }

        };
        msenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerManager.getWindowsController().openEditTextWindow(inputMsenseListener,msenseTextView.getText().toString(),null,null, InputType.TYPE_CLASS_NUMBER);
            }
        });
    }
    private void initElementOpacity(){
        elementOpacitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                doSetting(ELEMENT_OPACITY,String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                settingPreference.saveSetting(ELEMENT_OPACITY,String.valueOf(seekBar.getProgress()));
            }
        });
    }

    private void initMouseEnable(){
        mouseEnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String isCheckedString = String.valueOf(isChecked);
                doSetting(MOUSE_ENABLE,isCheckedString);
                settingPreference.saveSetting(MOUSE_ENABLE, isCheckedString);
            }
        });
    }

    private void initMouseMode(){
        doSetting(MOUSE_MODE,String.valueOf(true));
        mouseModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String isCheckedString = String.valueOf(isChecked);
                doSetting(MOUSE_MODE,isCheckedString);
                settingPreference.saveSetting(MOUSE_MODE, isCheckedString);
            }
        });
    }

    private void initSimplifyPerformance(){
        simplifyPerformanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String isCheckedString = String.valueOf(isChecked);
                doSetting(SIMPLIFY_PERFORMANCE,isCheckedString);
                settingPreference.saveSetting(SIMPLIFY_PERFORMANCE, isCheckedString);
            }
        });
        simplifyPerformanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                doSetting(SIMPLIFY_PERFORMANCE_OPACITY,String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                settingPreference.saveSetting(SIMPLIFY_PERFORMANCE_OPACITY,String.valueOf(seekBar.getProgress()));
            }
        });
    }




    public void loadSettingConfig(String configId){
        settingPreference = new SettingPreference(configId,context);
        Map<String, String> map = settingPreference.getSettings();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            disPlaySetting(key,value);
            doSetting(key,value);
        }

    }
    private void disPlaySetting(String settingName, String settingValue){
        switch (settingName){
            case MOUSE_SENSE:
                msenseTextView.setText(settingValue);
                break;
            case ELEMENT_OPACITY:
                elementOpacitySeekbar.setProgress(Integer.parseInt(settingValue));
                break;
            case MOUSE_ENABLE:
                mouseEnableSwitch.setChecked(Boolean.valueOf(settingValue));
                break;
            case MOUSE_MODE:
                mouseModeSwitch.setChecked(Boolean.valueOf(settingValue));
            case SIMPLIFY_PERFORMANCE:
                simplifyPerformanceSwitch.setChecked(Boolean.valueOf(settingValue));
                break;
            case SIMPLIFY_PERFORMANCE_OPACITY:
                simplifyPerformanceSeekBar.setProgress(Integer.parseInt(settingValue));
                break;
        }
    }
    private void doSetting(String settingName, String settingValue){
        switch (settingName){
            case MOUSE_SENSE:{
                controllerManager.getTouchController().adjustTouchSense(Integer.parseInt(settingValue));
                break;
            }
            case ELEMENT_OPACITY:
                controllerManager.getElementController().setOpacity(Integer.parseInt(settingValue) * 10);
                break;
            case MOUSE_ENABLE:
                controllerManager.getTouchController().enableTouch(Boolean.valueOf(settingValue));
                break;
            case MOUSE_MODE:
                boolean touchMode = Boolean.valueOf(settingValue);
                controllerManager.getTouchController().setTouchMode(touchMode);
                if (touchMode){
                    doSetting(MOUSE_SENSE,msenseTextView.getText().toString());
                }
                break;
            case SIMPLIFY_PERFORMANCE:
                if (Boolean.parseBoolean(settingValue)) {
                    controllerManager.getSimplifyPerformanceController().open();
                } else {
                    controllerManager.getSimplifyPerformanceController().close();
                }

                break;
            case SIMPLIFY_PERFORMANCE_OPACITY:
                controllerManager.getSimplifyPerformanceController().setOpacity(Integer.parseInt(settingValue) * (float)0.1);
                break;
        }
    }


    public void open(){
        settingLayout.setVisibility(View.VISIBLE);
    }

    public void close(){
        settingLayout.setVisibility(View.INVISIBLE);
    }
}
