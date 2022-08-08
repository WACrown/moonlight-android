package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;

import java.util.Map;
import java.util.regex.Pattern;

public class SelectKeyboardLayoutHelp {

    public static int initSharedPreferences(Context context){

        SharedPreferences pref = context.getSharedPreferences("keyboard_admin", Activity.MODE_PRIVATE);

        if (pref.contains("current_keyboard")){
            int currentNum = pref.getInt("current_keyboard",-1);
            if (currentNum > getLayoutCount(context) || currentNum < 0)
                setCurrentNum(context, 0);
        } else {
            setCurrentNum(context, 0);
        }

        if (pref.contains("all_keyboard_layout")){
            if (pref.getString("all_keyboard_layout","") == ""){
                LayoutList layoutList = new LayoutList();
                layoutList.add("keyboard_default");
                storeAllLayoutName(context,layoutList);

            }
        } else {
            LayoutList layoutList = new LayoutList();
            layoutList.add("keyboard_default");
            storeAllLayoutName(context,layoutList);
        }
        return 0;
    }

    public static LayoutList loadAllLayoutNameShow(final Context context){
        LayoutList layoutListValue = loadAllLayoutName(context);
        LayoutList layoutListKey = new LayoutList();
        for (String layoutValue : layoutListValue){
            layoutListKey.add(layoutValue.substring("keyboard_".length()));
        }
        return layoutListKey;
    }


    public static LayoutList loadAllLayoutName(final Context context) {
        LayoutList layoutNames = new LayoutList();
        SharedPreferences pref = context.getSharedPreferences("keyboard_admin", Activity.MODE_PRIVATE);
        String listString = pref.getString("all_keyboard_layout", "default");
        layoutNames.addStringToList(listString);
        return layoutNames;
    }

    public static String loadSingleLayoutNameShow(final Context context, final int index) {
        return loadAllLayoutName(context).get(index).substring("keyboard_".length());
    }

    public static String loadSingleLayoutName(final Context context, final int index) {
        return loadAllLayoutName(context).get(index);
    }

    public static int getLayoutCount(final Context context){
        return loadAllLayoutName(context).size();
    }

    public static int getCurrentNum(final Context context){
        SharedPreferences pref = context.getSharedPreferences("keyboard_admin", Activity.MODE_PRIVATE);
        int currentNum = pref.getInt("current_keyboard", 0);
        return currentNum;


    }

    public static int setCurrentNum(final Context context,int num){
        SharedPreferences.Editor prefEditor = context.getSharedPreferences("keyboard_admin", Activity.MODE_PRIVATE).edit();
        prefEditor.putInt("current_keyboard", num);
        prefEditor.apply();
        return 0;
    }


    public static int resetLayout(final Context context,final int layoutIndex){

        return 0;
    }


    public static int addLayout(final Context context,final String layoutName){
        if (!Pattern.matches("^[A-Za-z0-9]{1,25}$",layoutName)){
            //名字非法
            return 3;
        } else {
            String newLayoutNameFix = "keyboard_" + layoutName;
            LayoutList layoutList = loadAllLayoutName(context);
            if (layoutList.contains(newLayoutNameFix)){
                //已有名字
                return 2;
            } else {
                layoutList.add(newLayoutNameFix);
                System.out.println("addLayout: " + newLayoutNameFix);
                storeAllLayoutName(context,layoutList);
                return 0;
            }
        }
    }

    public static int renameLayout(final Context context,final int oldLayoutIndex, final String newLayoutName){
        if (!Pattern.matches("^[A-Za-z0-9]{1,25}$",newLayoutName)){
            //名字非法
            return 3;

        } else {
            String newLayoutNameFix = "keyboard_" + newLayoutName;
            LayoutList layoutList = loadAllLayoutName(context);
            if (layoutList.contains(newLayoutNameFix)) {
                //已有名字
                return 2;
            } else {
                SharedPreferences preferencesOld = context.getSharedPreferences(loadSingleLayoutName(context, oldLayoutIndex), Activity.MODE_PRIVATE);
                Map<String, ?> oldKeyboard = preferencesOld.getAll();
                SharedPreferences.Editor preferencesNew = context.getSharedPreferences(newLayoutNameFix, Activity.MODE_PRIVATE).edit();
                for (String key : oldKeyboard.keySet()) {
                    preferencesNew.putString(key, (String) oldKeyboard.get(key));
                }
                preferencesNew.apply();
                layoutList.set(oldLayoutIndex, newLayoutNameFix);
                storeAllLayoutName(context, layoutList);
                return 0;
            }
        }
    }


    public static int deleteLayout(final Context context,final int layoutIndex){
        if (getLayoutCount(context) < 2 ){
            //布局数量小于一时，不可以删除
            return 2;
        } else {
            LayoutList layoutList = loadAllLayoutName(context);
            SharedPreferences.Editor prefEditor = context.getSharedPreferences(layoutList.get(layoutIndex),Activity.MODE_PRIVATE).edit();
            prefEditor.clear().apply();
            layoutList.remove(layoutIndex);
            storeAllLayoutName(context,layoutList);
            setCurrentNum(context,0);
            return 0;
        }

    }

    private static int storeAllLayoutName(final Context context,final LayoutList layoutList) {
        SharedPreferences.Editor prefEditor = context.getSharedPreferences("keyboard_admin", Activity.MODE_PRIVATE).edit();
        prefEditor.putString("all_keyboard_layout", layoutList.toString());
        prefEditor.apply();
        return 0;
    }

}
