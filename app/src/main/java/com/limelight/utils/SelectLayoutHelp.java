package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

public class SelectLayoutHelp {


    public static int initSharedPreferences(Context context){

        SharedPreferences pref = context.getSharedPreferences("all_layout", Activity.MODE_PRIVATE);

        if (pref.contains("currentLayout")){
            int currentNum = pref.getInt("currentLayout",-1);
            if (currentNum > getLayoutCount(context) || currentNum < 0)
                setCurrentNum(context, 0);
        } else {
            setCurrentNum(context, 0);
        }

        if (pref.contains("layoutList")){
            if (pref.getString("layoutList","") == ""){
                LayoutList layoutList = new LayoutList();
                layoutList.add("default");
                storeAllLayoutName(context,layoutList);
                initLayout(context,"default");
            }
        } else {
            LayoutList layoutList = new LayoutList();
            layoutList.add("default");
            storeAllLayoutName(context,layoutList);
            initLayout(context,"default");
        }
        return 0;
    }

    public static LayoutList loadAllLayoutName(final Context context) {
        LayoutList layoutNames = new LayoutList();
        SharedPreferences pref = context.getSharedPreferences("all_layout", Activity.MODE_PRIVATE);
        String listString = pref.getString("layoutList", "default");
        layoutNames.addStringToList(listString);
        return layoutNames;
    }

    public static String loadSingleLayoutName(final Context context, final int index) {
        return loadAllLayoutName(context).get(index);
    }

    public static int getLayoutCount(final Context context){
        return loadAllLayoutName(context).size();
    }

    public static int getCurrentNum(final Context context){
        SharedPreferences pref = context.getSharedPreferences("all_layout", Activity.MODE_PRIVATE);
        int currentNum = pref.getInt("currentLayout", 0);
        return currentNum;


    }

    public static int setCurrentNum(final Context context,int num){
        SharedPreferences.Editor prefEditor = context.getSharedPreferences("all_layout", Activity.MODE_PRIVATE).edit();
        prefEditor.putInt("currentLayout", num);
        prefEditor.apply();
        return 0;
    }



    public static int addLayout(final Context context,final String layoutName){
        LayoutList layoutList = loadAllLayoutName(context);
        if (Pattern.matches("^[A-Za-z0-9]{1,25}$",layoutName)){
            if (layoutList.contains(layoutName)){
                //已有名字
                return 2;
            } else {
                layoutList.add(layoutName);
                storeAllLayoutName(context,layoutList);
                initLayout(context,layoutName);
                return 0;
            }
        } else {
            //名字非法
            return 3;
        }

    }

    public static int renameLayout(final Context context,final int oldLayoutIndex, final String newLayoutName){
        LayoutList layoutList = loadAllLayoutName(context);
        if (Pattern.matches("^[A-Za-z0-9]{1,25}$",newLayoutName)){
            if (layoutList.contains(newLayoutName)){
                //已有名字
                return 2;
            } else {
                SharedPreferences preferencesOld = context.getSharedPreferences(loadSingleLayoutName(context,oldLayoutIndex),Activity.MODE_PRIVATE);
                Map<String, ?> oldController = preferencesOld.getAll();
                SharedPreferences.Editor preferencesNew = context.getSharedPreferences(newLayoutName, Activity.MODE_PRIVATE).edit();
                for (String key : oldController.keySet()){
                    preferencesNew.putString(key, (String) oldController.get(key));
                }
                preferencesNew.apply();
                layoutList.set(oldLayoutIndex,newLayoutName);
                storeAllLayoutName(context,layoutList);
                return 0;
            }
        } else {
            return 3;
        }
    }

    public static int resetLayout(final Context context,final int layoutIndex){
        initLayout(context,loadSingleLayoutName(context,layoutIndex));
        return 0;
    }

    public static int deleteLayout(final Context context,final int layoutIndex){
        if (getLayoutCount(context) < 1 ){
            //布局数量小于一时，不可以删除
            return 2;
        } else {
            LayoutList layoutList = loadAllLayoutName(context);
            layoutList.remove(layoutIndex);
            storeAllLayoutName(context,layoutList);
            setCurrentNum(context,0);
            return 0;
        }

    }

    private static int storeAllLayoutName(final Context context,final LayoutList layoutList) {
        SharedPreferences.Editor prefEditor = context.getSharedPreferences("all_layout", Activity.MODE_PRIVATE).edit();
        prefEditor.putString("layoutList", layoutList.toString());
        prefEditor.apply();
        return 0;
    }

    private static int initLayout(final Context context,final String layoutName){
        SharedPreferences.Editor prefEditor = context.getSharedPreferences(layoutName, Activity.MODE_PRIVATE).edit();
        prefEditor.putString("1","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        prefEditor.putString("2","{\"LEFT\":14,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("3","{\"LEFT\":2133,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("4","{\"LEFT\":345,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("5","{\"LEFT\":1802,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        prefEditor.putString("6","{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("7","{\"LEFT\":2148,\"TOP\":158,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("8","{\"LEFT\":1860,\"TOP\":158,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("9","{\"LEFT\":2004,\"TOP\":14,\"WIDTH\":143,\"HEIGHT\":143}");
        prefEditor.putString("10","{\"LEFT\":489,\"TOP\":920,\"WIDTH\":172,\"HEIGHT\":100}");
        prefEditor.putString("11","{\"LEFT\":1673,\"TOP\":920,\"WIDTH\":172,\"HEIGHT\":100}");
        prefEditor.putString("12","{\"LEFT\":86,\"TOP\":57,\"WIDTH\":374,\"HEIGHT\":374}");
        prefEditor.putString("13","{\"LEFT\":1889,\"TOP\":604,\"WIDTH\":374,\"HEIGHT\":374}");
        prefEditor.apply();
        return 0;
    }


}
