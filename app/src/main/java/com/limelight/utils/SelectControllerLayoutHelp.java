package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.util.Size;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SelectControllerLayoutHelp {


    public static int initSharedPreferences(Context context){

        SharedPreferences pref = context.getSharedPreferences("controller_admin", Activity.MODE_PRIVATE);

        if (pref.contains("current_controller")){
            int currentNum = pref.getInt("current_controller",-1);
            if (currentNum > getLayoutCount(context) || currentNum < 0)
                setCurrentNum(context, 0);
        } else {
            setCurrentNum(context, 0);
        }

        if (pref.contains("all_controller_layout")){
            if (pref.getString("all_controller_layout","") == ""){
                LayoutList layoutList = new LayoutList();
                layoutList.add("controller_default");
                storeAllLayoutName(context,layoutList);
                initLayout(context,"controller_default");
            }
        } else {
            LayoutList layoutList = new LayoutList();
            layoutList.add("controller_default");
            storeAllLayoutName(context,layoutList);
            initLayout(context,"controller_default");
        }
        return 0;
    }

    public static LayoutList loadAllLayoutNameShow(final Context context){
        LayoutList layoutListValue = loadAllLayoutName(context);
        LayoutList layoutListKey = new LayoutList();
        for (String layoutValue : layoutListValue){
            layoutListKey.add(layoutValue.substring("controller_".length()));
        }
        return layoutListKey;
    }

    public static LayoutList loadAllLayoutName(final Context context) {
        LayoutList layoutNames = new LayoutList();
        SharedPreferences pref = context.getSharedPreferences("controller_admin", Activity.MODE_PRIVATE);
        String listString = pref.getString("all_controller_layout", "controller_default");
        layoutNames.addStringToList(listString);
        return layoutNames;
    }

    public static String loadSingleLayoutNameShow(final Context context, final int index) {
        return loadAllLayoutName(context).get(index).substring("controller_".length());
    }

    public static String loadSingleLayoutName(final Context context, final int index) {
        return loadAllLayoutName(context).get(index);
    }

    public static int getLayoutCount(final Context context){
        return loadAllLayoutName(context).size();
    }

    public static int getCurrentNum(final Context context){
        SharedPreferences pref = context.getSharedPreferences("controller_admin", Activity.MODE_PRIVATE);
        int currentNum = pref.getInt("current_controller", 0);
        return currentNum;


    }

    public static int setCurrentNum(final Context context,int num){
        SharedPreferences.Editor prefEditor = context.getSharedPreferences("controller_admin", Activity.MODE_PRIVATE).edit();
        prefEditor.putInt("current_controller", num);
        prefEditor.apply();
        return 0;
    }



    public static int addLayout(final Context context,final String layoutName){
        if (!Pattern.matches("^[A-Za-z0-9]{1,25}$",layoutName)){
            //名字非法
            return 3;
        } else {
            String newLayoutNameFix = "controller_" + layoutName;
            LayoutList layoutList = loadAllLayoutName(context);
            if (layoutList.contains(newLayoutNameFix)){
                //已有名字
                return 2;
            } else {
                layoutList.add(newLayoutNameFix);
                System.out.println("addLayout: " + newLayoutNameFix);
                storeAllLayoutName(context,layoutList);
                initLayout(context,newLayoutNameFix);
                return 0;
            }
        }
    }

    public static int renameLayout(final Context context,final int oldLayoutIndex, final String newLayoutName){
        if (!Pattern.matches("^[A-Za-z0-9]{1,25}$",newLayoutName)){
            //名字非法
            return 3;

        } else {
            String newLayoutNameFix = "controller_" + newLayoutName;
            LayoutList layoutList = loadAllLayoutName(context);
            if (layoutList.contains(newLayoutNameFix)) {
                //已有名字
                return 2;
            } else {
                SharedPreferences preferencesOld = context.getSharedPreferences(loadSingleLayoutName(context, oldLayoutIndex), Activity.MODE_PRIVATE);
                Map<String, ?> oldController = preferencesOld.getAll();
                SharedPreferences.Editor preferencesNew = context.getSharedPreferences(newLayoutNameFix, Activity.MODE_PRIVATE).edit();
                for (String key : oldController.keySet()) {
                    preferencesNew.putString(key, (String) oldController.get(key));
                }
                preferencesNew.apply();
                layoutList.set(oldLayoutIndex, newLayoutNameFix);
                storeAllLayoutName(context, layoutList);
                return 0;
            }
        }
    }

    public static int resetLayout(final Context context,final int layoutIndex){
        initLayout(context,loadSingleLayoutName(context,layoutIndex));
        return 0;
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
        SharedPreferences.Editor prefEditor = context.getSharedPreferences("controller_admin", Activity.MODE_PRIVATE).edit();
        prefEditor.putString("all_controller_layout", layoutList.toString());
        prefEditor.apply();
        return 0;
    }

    private static int initLayout(final Context context,final String layoutName){
        SharedPreferences.Editor prefEditor = context.getSharedPreferences(layoutName, Activity.MODE_PRIVATE).edit();
        prefEditor.putString("EID_DPAD","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        prefEditor.putString("EID_LT","{\"LEFT\":14,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("EID_RT","{\"LEFT\":2133,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("EID_LB","{\"LEFT\":345,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("EID_RB","{\"LEFT\":1802,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("EID_A","{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("EID_B","{\"LEFT\":2148,\"TOP\":158,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("EID_X","{\"LEFT\":1860,\"TOP\":158,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("EID_Y","{\"LEFT\":2004,\"TOP\":14,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("EID_BACK","{\"LEFT\":489,\"TOP\":920,\"WIDTH\":172,\"HEIGHT\":100}");
        prefEditor.putString("EID_START","{\"LEFT\":1673,\"TOP\":920,\"WIDTH\":172,\"HEIGHT\":100}");
        prefEditor.putString("EID_LS","{\"LEFT\":86,\"TOP\":57,\"WIDTH\":374,\"HEIGHT\":374}");
        prefEditor.putString("EID_RS","{\"LEFT\":1889,\"TOP\":604,\"WIDTH\":374,\"HEIGHT\":374}");
        prefEditor.apply();
        return 0;
    }



}
