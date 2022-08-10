package com.limelight.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LayoutAdmin {

    private final String CURRENT_NUM = "current_num";
    private final String ALL_LAYOUT = "all_layout";

    private final String ADMIN_TABLE;
    private final String PRE_LAYOUT;
    private LayoutList layoutList;
    private int currentNum;
    private final Context context;
    private final String name;

    public LayoutAdmin(Context context, String processor) {
        this.ADMIN_TABLE = processor + "_admin";
        this.PRE_LAYOUT = processor + "_";
        this.context = context;
        this.name = processor;

        if (getAllLayoutFromTable() != 0){
            layoutList.add("default");
            setAllLayoutToTable();
        }

        if (getCurrentLayoutNumFromTable() != 0){
            setCurrentLayoutNumToTable();
        }

    }


    public LayoutList getLayoutList() {
        return layoutList;
    }


    public int addLayout(String layoutName) {

        if (layoutList.contains(layoutName)){
            return -1;
        }

        layoutList.add(layoutName);
        setAllLayoutToTable();
        return 0;
    }


    public int deleteLayout(int layoutNum) {

        if (layoutNum > layoutList.size() - 1){
            return -1;
        }

        layoutList.remove(layoutNum);
        setAllLayoutToTable();
        return 0;
    }


    public int updateLayout(int layoutNum, String layoutName) {

        if (layoutNum > layoutList.size() - 1 || layoutList.contains(layoutName)){
            return -1;
        }

        layoutList.set(layoutNum,layoutName);
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

    private int getAllLayoutFromTable() {
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

    private int setAllLayoutToTable() {
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

    @Override
    public String toString() {
        return name;
    }
}
