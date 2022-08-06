package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class EditKeyboardLayoutHelp {

    public static int addKeyboardButton(final Context context, String key, String layoutName){

        SharedPreferences.Editor prefEditor = context.getSharedPreferences(layoutName, Activity.MODE_PRIVATE).edit();
        prefEditor.putString(key,"{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.apply();
        return 0;
    }

    public static int deleteKeyboardButton(final Context context,String deleteName, String layoutName){

        SharedPreferences.Editor prefEditor = context.getSharedPreferences(layoutName, Activity.MODE_PRIVATE).edit();
        prefEditor.remove(deleteName);
        prefEditor.apply();
        return 0;
    }

    public static Set<String> getAllButtonSet(final Context context, String layoutName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(layoutName, Activity.MODE_PRIVATE);
        Map<String,?> allButtonMap =  sharedPreferences.getAll();
        return allButtonMap.keySet();
    }

    public static int isInvalid(String keyName){
        String[] keysAndName = keyName.split("-");
        if ((keysAndName.length == 2 )&& Pattern.matches("^[A-Za-z0-9]{1,5}$",keysAndName[keysAndName.length-1])){
            if (VirtualControllerConfigurationLoader.getKeycode(keysAndName[0]) != -1){
                return 0;
            } else {
                //没有此按键
                return -2;
            }
            //验证PAD名称是否合法

        } else if ((keysAndName.length == 6 && keysAndName[0].equals("PAD")) && (Pattern.matches("^[A-Za-z0-9]{1,5}$",keysAndName[keysAndName.length-1]))){
            if ((VirtualControllerConfigurationLoader.getKeycode(keysAndName[1]) != -1 && VirtualControllerConfigurationLoader.getKeycode(keysAndName[2]) != -1) && (VirtualControllerConfigurationLoader.getKeycode(keysAndName[3]) != -1 && VirtualControllerConfigurationLoader.getKeycode(keysAndName[4]) != -1)) {
                return 0;
            } else {
                return -3;
            }

        } else {
            //名字非法
            return -1;
        }

    }

}
