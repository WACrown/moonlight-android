package com.limelight.utils.controller;

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
        this.preference = "keyboard_" + preference;
        this.map = SharedPreferencesHelper.load(context, preference);
    }

    @Override
    public int init() {
        map.clear();
        SharedPreferencesHelper.store(context, preference, map);
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
        System.out.println("wangguan db " + preference + "  " + map);
        if (key.split("-").length == 2) {
            //BUTTON
            map.put(key, "{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
            SharedPreferencesHelper.store(context, preference, map);
            return 0;
        } else if (key.split("-").length == 6) {
            //PAD
            map.put(key, "{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
            SharedPreferencesHelper.store(context, preference, map);
            return 0;
        } else if (key.split("-").length == 7) {
            //STICK
            map.put(key, "{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
            SharedPreferencesHelper.store(context, preference, map);
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
        System.out.println("wangguan db " + preference + "  " + map);
        SharedPreferencesHelper.store(context, preference, map);
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public int update(String key, String newValue) {

        if (!isExist(key)){
            return -1;
        }
        map.replace(key, newValue);
        SharedPreferencesHelper.store(context, preference, map);
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
            //验证PAD名称是否合法,合法PAD：PAD-W-S-A-D-1
            for (int i = 1;i < keysAndName.length-1; i ++) {
                //依次验证所有设置的按键存不存在
                if (VirtualControllerConfigurationLoader.getKeycode(keysAndName[i]) == -1){
                    return -4; //按键映射表中没有此按键
                }
            }
            return 0;

        } else if ((keysAndName.length == 7 && keysAndName[0].equals("STICK")) && (Pattern.matches("^[A-Za-z0-9]{1,5}$",keysAndName[keysAndName.length-1]))){
            //验证STICK名称是否合法,合法PAD：STK-W-S-A-D-SHIFT-1
            for (int i = 1;i < keysAndName.length-1; i ++) {
                //依次验证所有设置的按键存不存在
                if (VirtualControllerConfigurationLoader.getKeycode(keysAndName[i]) == -1){
                    return -4; //按键映射表中没有此按键
                }
            }
            return 0;
        } else {
            //格式非法
            return -2;
        }
    }

    private boolean isExist(String key) {
        return map.containsKey(key);
    }
}
