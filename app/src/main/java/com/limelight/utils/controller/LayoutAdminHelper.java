package com.limelight.utils.controller;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LayoutAdminHelper {

    private static LayoutList layoutList = new LayoutList();
    private static int currentLayoutNum = 0;

    public static void initHelp(Context context){

        if (SharedPreferencesHelper.load(context,"layout_admin").size() != 2) {
            Map<String, String> layoutAdminTable = new HashMap<>();
            layoutAdminTable.put("current_num","0");
            layoutAdminTable.put("all_layout","default");
            SharedPreferencesHelper.store(context,"layout_admin",layoutAdminTable);
            LayoutEditHelper.init(context,"default");
        }

        Map<String ,String> layoutAdminTable =  SharedPreferencesHelper.load(context,"layout_admin");
        currentLayoutNum = Integer.parseInt(layoutAdminTable.get("current_num"));
        String layoutListString = layoutAdminTable.get("all_layout");
        layoutList.StringToList(layoutListString);
    }


    public static int addLayout(Context context,String layoutName){
        if (isInvalid(layoutName)){
            return -2;//非法
        }

        if (isExist(layoutName)){
            return -1;//已存在
        }



        layoutList.add(layoutName);
        saveAllLayoutToTable(context);
        LayoutEditHelper.init(context,layoutName);
        return 0;

    }

    public static int renameCurrentLayout(Context context, String newLayoutName){

        if (isInvalid(newLayoutName)){
            return -2;//非法
        }

        if (isExist(newLayoutName)){
            return -1;//已存在
        }


        //复制表中的数值到新表，删除旧表
        Map<String, String> allButton = LayoutEditHelper.loadAllConf(context);
        LayoutEditHelper.storeAllConf(context,new HashMap<>());
        layoutList.set(currentLayoutNum,newLayoutName);
        LayoutEditHelper.storeAllConf(context,allButton);
        saveAllLayoutToTable(context);
        return 0;

    }

    public static int deleteCurrentLayout(Context context){

        if (layoutList.size() == 1){
            return -5; //至少存在一个布局
        }

        layoutList.remove(currentLayoutNum);
        saveAllLayoutToTable(context);
        return 0;
    }

    public static void selectLayout(Context context,String layoutName){
        currentLayoutNum = layoutList.indexOf(layoutName);
        saveAllLayoutToTable(context);
    }

    public static void selectLayout(Context context,int layoutIndex){
        currentLayoutNum = layoutIndex;
        saveAllLayoutToTable(context);
    }




    public static LayoutList getLayoutList(Context context){
        return layoutList;
    }

    public static int getCurrentLayoutNum(Context context){
        return currentLayoutNum;
    }

    public static String getCurrentLayoutName(Context context){
        return layoutList.get(currentLayoutNum);
    }

    private static boolean isInvalid(String layoutName){
        return !Pattern.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]{1,10}$",layoutName);
    }

    private static boolean isExist(String layoutName){
        return layoutList.contains(layoutName);
    }

    private static void saveAllLayoutToTable(Context context){
        Map<String, String> layoutAdminTable = new HashMap<>();
        layoutAdminTable.put("current_num",""+currentLayoutNum);
        layoutAdminTable.put("all_layout",""+layoutList.toString());
        SharedPreferencesHelper.store(context,"layout_admin",layoutAdminTable);
    }

}
