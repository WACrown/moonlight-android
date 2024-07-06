package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.limelight.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigUIController extends UIController {



    private FrameLayout layerConfig;
    private ConfigListPreference configListPreference;
    private Context context;
    private ConfigItem currentSelectItem;
    private List<ConfigItem> configItemList = new ArrayList<>();

    private ControllerManager controllerManager;

    private LinearLayout configItemContainer;



    public ConfigUIController(ControllerManager controllerManager, FrameLayout layout, Context context){
        this.context = context;
        this.layerConfig = layout;
        this.controllerManager = controllerManager;
        configListPreference = new ConfigListPreference(context);
        configItemContainer = layout.findViewById(R.id.config_item_container);
        layout.findViewById(R.id.add_config_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAddWindow();
            }
        });
        loadAllConfigToList();
    }

    private void loadAllConfigToList(){
        for (Map.Entry<String,String> entry: configListPreference.getSortedConfigurationMap().entrySet()){
            String configId = entry.getKey();
            configItemList.add(addConfigItemToList(configId,entry.getValue()));
        }

    }

    private void addConfigItem(String configName){
        String configId = String.valueOf(System.currentTimeMillis());
        addConfigItemToPreference(configId, configName);
        addConfigItemToList(configId, configName);
    }

    private ConfigItem addConfigItemToList(String configId, String configName){
        ConfigItem configItem = new ConfigItem(this,configName,configId,context);
        configItemContainer.addView(configItem.getView(), (configItemContainer.getChildCount() - 1));
        return configItem;
    }
    private void addConfigItemToPreference(String configId, String configName){
        configListPreference.addConfiguration(configId,configName);
    }

    private void deleteConfigItem(ConfigItem configItem){
        deleteConfigItemFromList(configItem);
        deleteConfigItemFromPreference(configItem);
    }

    private void deleteConfigItemFromList(ConfigItem configItem){
        configItemContainer.removeView(configItem.getView());
        configItemList.remove(configItem);
    }

    private void deleteConfigItemFromPreference(ConfigItem configItem){
        String configId = configItem.getId();
        //1.先把element的preference删除
        new ElementPreference(configId, context).delete();
        //2.再把setting的preference删除
        new SettingPreference(configId,context).delete();
        //3.删除配置列表中的名字
        configListPreference.deleteConfig(configId);
    }

    private void renameConfigItem(ConfigItem configItem, String nowName){
        renameConfigToList(configItem, nowName);
        renameConfigToPreference(configItem, nowName);
    }

    private void renameConfigToList(ConfigItem configItem, String nowName){
        configItem.setName(nowName);
    }

    private void renameConfigToPreference(ConfigItem configItem, String nowName){
        configListPreference.renameConfiguration(configItem.getId(),nowName);
    }

    public String getCurrentConfigId(){
        return configListPreference.getCurrentConfigId();
    }

    private ConfigItem getCurrentConfigItem(){
        for (ConfigItem configItem : configItemList){
            if (configItem.getId().equals(getCurrentConfigId())){
                return configItem;
            }
        }
        ConfigItem configItem = configItemList.get(0);
        configListPreference.setCurrentConfigId(configItem.getId());
        return configItem;
    }


    private void loadConfig(String configId){
        controllerManager.getElementController().loadElementConfig(configId);
        controllerManager.getSettingController().loadSettingConfig(configId);
    }

    public void initLoadCurrentConfig(){
        selectItem(getCurrentConfigItem());
    }


    public void selectItem(ConfigItem configItem){
        if (currentSelectItem == configItem){
            return;
        }
        if (currentSelectItem != null){
            currentSelectItem.unselected();
        }
        currentSelectItem = configItem;
        currentSelectItem.selected();
        loadConfig(configItem.getId());
        configListPreference.setCurrentConfigId(configItem.getId());
    }

    public void jumpAddWindow(){
        WindowsController.EditTextWindowListener addListener = new WindowsController.EditTextWindowListener() {
            @Override
            public boolean onConfirmClick(String text) {
                if (configListPreference.isContainedName(text)){
                    Toast.makeText(context,"配置名字已存在",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!text.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,15}$")){
                    Toast.makeText(context,"名称不能有符号，且长度为1-15个字符",Toast.LENGTH_SHORT).show();
                    return false;
                }
                addConfigItem(text);
                return true;
            }

            @Override
            public void onCancelClick() {
            }
        };
        controllerManager.getWindowsController().openEditTextWindow(addListener,"","","", InputType.TYPE_CLASS_TEXT);
    }



    public void jumpRenameWindow(ConfigItem configItem){
        WindowsController.EditTextWindowListener renameListener = new WindowsController.EditTextWindowListener() {
            @Override
            public boolean onConfirmClick(String text) {
                String nowName = text;
                //如果名字没有改变，点击确认键也可以返回，如果没有这个判断，点击确认键会显示名称已存在
                if (configItem.getName().equals(nowName)){
                    return true;
                }
                if (configListPreference.isContainedName(nowName)){
                    Toast.makeText(context,"配置名字已存在",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!nowName.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,15}$")){
                    Toast.makeText(context,"名称不能有符号，且长度为1-15个字符",Toast.LENGTH_SHORT).show();
                    return false;
                }
                renameConfigItem(configItem, nowName);
                return true;
            }

            @Override
            public void onCancelClick() {
            }

        };
        controllerManager.getWindowsController().openEditTextWindow(renameListener,configItem.getName(),"","",InputType.TYPE_CLASS_TEXT);
    }

    public void jumpDeleteWindow(ConfigItem configItem){
        WindowsController.TextWindowListener deleteListener = new WindowsController.TextWindowListener() {
            @Override
            public boolean onConfirmCLick() {
                deleteConfigItem(configItem);
                return true;
            }

            @Override
            public void onCancelClick() {
            }

        };

        controllerManager.getWindowsController().openTextWindow(deleteListener, "是否删除:" + configItem.getName());
    }

    public void open(){
        layerConfig.setVisibility(View.VISIBLE);
        controllerManager.setOpenedController(this);
    }

    public void close(){
        layerConfig.setVisibility(View.GONE);
        controllerManager.setOpenedController(null);
    }




}
