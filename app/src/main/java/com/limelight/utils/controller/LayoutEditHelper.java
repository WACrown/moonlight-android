package com.limelight.utils.controller;

import android.content.Context;

import com.limelight.preferences.StreamSettings;

import java.util.HashMap;
import java.util.Map;

public class LayoutEditHelper {

    public static void init(Context context, String layoutName){
        Map<String, String> allButton = new HashMap<>();
        allButton.put("GP-LS-0","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        SharedPreferencesHelper.store(context,layoutName,allButton);
    }

    public static int storeAllButton(Context context,Map<String, String> allButton){
        return SharedPreferencesHelper.store(context,LayoutAdminHelper.getCurrentLayoutName(context),allButton);
    }

    public static Map<String, String> loadAllButton(Context context){
        return SharedPreferencesHelper.load(context,LayoutAdminHelper.getCurrentLayoutName(context));
    }

}
