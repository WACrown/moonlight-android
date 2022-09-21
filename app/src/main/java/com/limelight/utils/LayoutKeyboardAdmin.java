package com.limelight.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LayoutKeyboardAdmin extends LayoutAdmin{

    private final String CURRENT_NUM = "current_num";
    private final String ALL_LAYOUT = "all_layout";

    private final String ADMIN_TABLE = "keyboard_admin";
    private final String PRE_LAYOUT = "keyboard_";
    private LayoutList layoutList;
    private int currentNum;
    private final Context context;

    public LayoutKeyboardAdmin(Context context) {
        this.context = context;

        if (loadAllLayoutFromTable() != 0){
            layoutList.add("default");
            saveAllLayoutToTable();
        }

        if (getCurrentLayoutNumFromTable() != 0){
            setCurrentLayoutNumToTable();
        }

    }


    public LayoutList getLayoutList() {
        return layoutList;
    }


    public int addLayout(String layoutName) {

        if (isExist(layoutName)){
            return -1;//已存在
        }

        if (isInvalid(layoutName)){
            return -2;//非法
        }

        layoutList.add(layoutName);
        saveAllLayoutToTable();
        LayoutEdit layoutEdit = new LayoutKeyboardEdit(context,layoutName);
        layoutEdit.init();
        return 0;
    }


    public int deleteLayout(int layoutNum) {

        if (layoutNum > layoutList.size() - 1){
            return -3; //超出数组长度，布局不存在
        }

        if (layoutList.size() == 1){
            return -5; //至少存在一个布局
        }

        layoutList.remove(layoutNum);
        saveAllLayoutToTable();
        return 0;
    }


    public int updateLayout(int layoutNum, String layoutName) {

        if (isExist(layoutName)){
            return -1;
        }

        if (layoutNum > layoutList.size() - 1){
            return -3;
        }

        layoutList.set(layoutNum,layoutName);
        saveAllLayoutToTable();
        return 0;
    }



    public int getCurrentLayoutNum() {
        return currentNum;
    }


    public int setCurrentLayoutNum(int currentNum) {

        if (currentNum > layoutList.size() - 1 || currentNum < 0){
            return -1;
        }

        this.currentNum = currentNum;
        setCurrentLayoutNumToTable();
        return 0;
    }

    private int loadAllLayoutFromTable() {
        //layoutList中layout名字格式default
        //layoutNamesFixed中layout名字格式controller_default
        layoutList = new LayoutList();
        LayoutList layoutNamesFixed = new LayoutList();
        String listString = SharedPreferencesHelp.load(context, ADMIN_TABLE, ALL_LAYOUT);

        if (listString == null) {
            return -1;
        }

        layoutNamesFixed.addStringToList(listString);

        //layoutNamesControllerFixed → layoutNames
        for (String layoutNameControllerFixed : layoutNamesFixed){
            layoutList.add(layoutNameControllerFixed.substring(PRE_LAYOUT.length()));
        }

        if (layoutList.size() < 1){
            return -2;
        }

        return 0;
    }

    private int saveAllLayoutToTable() {
        //layoutList
        //layoutNamesFixed中layout名字格式controller_default
        //layoutList → layoutNamesControllerFixed
        LayoutList layoutNamesControllerFixed = new LayoutList();
        for (String layoutName : layoutList){
            layoutNamesControllerFixed.add(PRE_LAYOUT + layoutName);
        }

        Map<String, String> map = new HashMap<>();
        map.put(ALL_LAYOUT,layoutNamesControllerFixed.toString());
        SharedPreferencesHelp.store(context, ADMIN_TABLE, map);

        return 0;
    }


    private int getCurrentLayoutNumFromTable(){

        String currentNumString = SharedPreferencesHelp.load(context, ADMIN_TABLE, CURRENT_NUM);

        if (currentNumString == null) {
            //如果CURRENT_NUM的值为空
            currentNum = 0;
            return -1;
        }

        currentNum = Integer.parseInt(currentNumString) ;

        if (currentNum > layoutList.size() - 1) {
            //如果currentNum值不对
            currentNum = 0;
            return -2;
        }

        return 0;

    }

    private int setCurrentLayoutNumToTable(){

        String currentNumString = String.valueOf(currentNum);

        Map<String, String> map = new HashMap<>();
        map.put(CURRENT_NUM,currentNumString);
        SharedPreferencesHelp.store(context, ADMIN_TABLE, map);

        return 0;

    }

    private boolean isInvalid(String layoutName){
        return !Pattern.matches("^[A-Za-z0-9]{1,25}$",layoutName);
    }

    private boolean isExist(String layoutName){
        return layoutList.contains(layoutName);
    }

    @Override
    public String toString() {
        return PRE_LAYOUT;
    }
}
