package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.R;

import java.util.Map;

public class PageSettingController {

    private static final String MOUSE_SENSE = "mouse_sense";
    private static final String ELEMENT_OPACITY = "element_opacity";
    private static final String MOUSE_ENABLE = "mouse_enable";
    private static final String MOUSE_MODE = "mouse_mode";
    private static final String SIMPLIFY_PERFORMANCE = "simplify_performance";
    private static final String SIMPLIFY_PERFORMANCE_OPACITY = "simplify_performance_opacity";

    private SettingPreference settingPreference;
    private ControllerManager controllerManager;
    private SuperPageLayout settingLayout;
    //simplifyPerformance


    private TextView msenseTextView;
    private SeekBar elementOpacitySeekbar;
    private Switch mouseEnableSwitch;
    private Switch mouseModeSwitch;
    private Switch simplifyPerformanceSwitch;
    private SeekBar simplifyPerformanceSeekBar;

    private Context context;

    public PageSettingController(ControllerManager controllerManager, Context context){
        this.controllerManager = controllerManager;
        this.settingLayout = (SuperPageLayout) LayoutInflater.from(context).inflate(R.layout.page_setting,null);
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

        SuperPageLayout inputWindow = (SuperPageLayout) LayoutInflater.from(context).inflate(R.layout.page_input,null);
        TextView inputWindowTitle = inputWindow.findViewById(R.id.window_input_title);
        EditText inputWindowEdittext = inputWindow.findViewById(R.id.window_input_edittext);
        TextView inputWindowConfirm = inputWindow.findViewById(R.id.window_input_confirm);
        TextView inputWindowCancel = inputWindow.findViewById(R.id.window_input_cancel);

        inputWindowTitle.setText("灵敏度:");
        inputWindowEdittext.setText(msenseTextView.getText());
        inputWindowConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputWindowEdittext.getText().toString();
                if (text.equals("")){
                    Toast.makeText(context,"请输入" + min + "~" + max + "的数字",Toast.LENGTH_SHORT).show();
                    return;
                }
                int value = Integer.parseInt(text);
                if (value > max || value < min){
                    Toast.makeText(context,"请输入" + min + "~" + max + "的数字",Toast.LENGTH_SHORT).show();
                    return;
                }
                String sense = String.valueOf(value);
                msenseTextView.setText(sense);
                doSetting(MOUSE_SENSE, sense);
                settingPreference.saveSetting(MOUSE_SENSE, sense);
                controllerManager.getSuperPagesController().close();
            }
        });

        inputWindowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerManager.getSuperPagesController().close();
            }
        });


        msenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerManager.getSuperPagesController().open(inputWindow);
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
                    doSetting(MOUSE_SENSE, msenseTextView.getText().toString());
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
        controllerManager.getSuperPagesController().open(settingLayout);
    }

    public void close(){

    }
}
