package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.preferences.StreamSettings;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class LayoutEditHelper {

    private static LayoutProcessor layoutProcessor;

    public static int addKeyboardButton(String name){

        if (isInvalid(name) != 0){
            return -1;
        }

        if (isExist(name) == 0) {
            return -2;
        }

        if (name.split("-").length == 2) {
            layoutProcessor.add(name,"{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
        } else if (name.split("-").length == 6) {
            layoutProcessor.add(name,"{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        }
        return 0;
    }

    public static int deleteKeyboardButton(String deleteName){
        if (isInvalid(deleteName) != 0){
            return -1;
        }

        if (isExist(deleteName) == -2) {
            return -2;
        }

        layoutProcessor.delete(deleteName);
        return 0;
    }


    private static int isExist(String key){
        if (layoutProcessor.get().containsKey(key)){
            return 0;
        }

        return -2;

    }


    private static int isInvalid(String keyName){
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
