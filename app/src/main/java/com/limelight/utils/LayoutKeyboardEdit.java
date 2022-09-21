package com.limelight.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;

import java.util.Map;
import java.util.regex.Pattern;

public class LayoutKeyboardEdit extends LayoutEdit {
    //需要修改controller_表的名字，例如controller_default
    private final String preference;
    private Map<String, String> map;
    private final Context context;

    public LayoutKeyboardEdit(Context context, String preference) {
        this.context = context;
        this.preference = preference;
        this.map = SharedPreferencesHelp.load(context, preference);
    }

    @Override
    public int init() {
        map.clear();
        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }

    @Override
    public Map<String, String> get() {
        return map;
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public int add(String key) {
        if (isExist(key)) {
            return -1;//已存在
        }

        if (isInvalid(key) != 0) {
            return -2;//格式非法
        }

        if (key.split("-").length == 2) {
            map.put(key, "{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
            SharedPreferencesHelp.store(context, preference, map);
            return 0;
        } else if (key.split("-").length == 6) {
            map.put(key, "{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
            SharedPreferencesHelp.store(context, preference, map);
            return 0;
        } else {
            return -2; //格式非法
        }
    }

    @Override
    public int delete(String key) {
        if (!isExist(key)){
            return -3; //名字不存在
        }
        map.remove(key);
        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public int update(String key, String newValue) {

        if (!isExist(key)){
            return -1;
        }
        map.replace(key, newValue);
        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }

    private int isInvalid(String keyName) {
        String[] keysAndName = keyName.split("-");
        if ((keysAndName.length == 2 )&& Pattern.matches("^[A-Za-z0-9]{1,5}$",keysAndName[keysAndName.length-1])){
            if (VirtualControllerConfigurationLoader.getKeycode(keysAndName[0]) != -1){
                return 0;
            } else {
                //按键映射表中没有此按键
                return -4;
            }
        } else if ((keysAndName.length == 6 && keysAndName[0].equals("PAD")) && (Pattern.matches("^[A-Za-z0-9]{1,5}$",keysAndName[keysAndName.length-1]))){
            //验证PAD名称是否合法
            if ((VirtualControllerConfigurationLoader.getKeycode(keysAndName[1]) != -1 && VirtualControllerConfigurationLoader.getKeycode(keysAndName[2]) != -1) && (VirtualControllerConfigurationLoader.getKeycode(keysAndName[3]) != -1 && VirtualControllerConfigurationLoader.getKeycode(keysAndName[4]) != -1)) {
                return 0;
            } else {
                //PAD按键组中有按键映射表中没有的按键
                return -4;
            }

        } else {
            //格式非法
            return -2;
        }
    }

    private boolean isExist(String key) {
        return map.containsKey(key);
    }
}
