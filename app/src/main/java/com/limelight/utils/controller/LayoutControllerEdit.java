package com.limelight.utils.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.util.Map;

public class LayoutControllerEdit extends LayoutEdit {

    private final String preference;
    private Map<String, String> map;
    private final Context context;

    public LayoutControllerEdit(Context context, String preference) {
        this.context = context;
        this.preference = "controller_"+ preference;
        this.map = SharedPreferencesHelper.load(context, preference);
    }

    @Override
    public int init() {
        map.clear();
        map.put("EID_DPAD","{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
        map.put("EID_LT","{\"LEFT\":14,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        map.put("EID_RT","{\"LEFT\":2133,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        map.put("EID_LB","{\"LEFT\":345,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        map.put("EID_RB","{\"LEFT\":1802,\"TOP\":446,\"WIDTH\":172,\"HEIGHT\":129}");
        map.put("EID_A","{\"LEFT\":2004,\"TOP\":302,\"WIDTH\":143,\"HEIGHT\":143}");
        map.put("EID_B","{\"LEFT\":2148,\"TOP\":158,\"WIDTH\":143,\"HEIGHT\":143}");
        map.put("EID_X","{\"LEFT\":1860,\"TOP\":158,\"WIDTH\":143,\"HEIGHT\":143}");
        map.put("EID_Y","{\"LEFT\":2004,\"TOP\":14,\"WIDTH\":143,\"HEIGHT\":143}");
        map.put("EID_BACK","{\"LEFT\":489,\"TOP\":920,\"WIDTH\":172,\"HEIGHT\":100}");
        map.put("EID_START","{\"LEFT\":1673,\"TOP\":920,\"WIDTH\":172,\"HEIGHT\":100}");
        map.put("EID_LS","{\"LEFT\":86,\"TOP\":57,\"WIDTH\":374,\"HEIGHT\":374}");
        map.put("EID_RS","{\"LEFT\":1889,\"TOP\":604,\"WIDTH\":374,\"HEIGHT\":374}");
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

//        if (map.containsKey(key)){
//            return -1;
//        }
//        map.put(key, value);
//        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }

    @Override
    public int delete(String key) {
        if (!map.containsKey(key)){
            return -1;
        }
        map.remove(key);
        SharedPreferencesHelper.store(context, preference, map);
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public int update(String key, String newValue) {

        if (!map.containsKey(key)){
            return -1;
        }
        map.replace(key, newValue);
        SharedPreferencesHelper.store(context, preference, map);
        return 0;
    }


    private int isInvalid(String keyName) {
        return 0;
    }


    private boolean isExist(String key) {
        return true;
    }
}
