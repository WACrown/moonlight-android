package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelp {

    public static String load(Context context, String preference, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    public static Map<String, String> load(Context context, String preference){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Activity.MODE_PRIVATE);
        return (Map<String, String>) sharedPreferences.getAll();
    }

    public static int store(Context context, String preference, Map<String, String> map){
        SharedPreferences.Editor preEdit= context.getSharedPreferences(preference, Activity.MODE_PRIVATE).edit();
        for (String key : map.keySet()){
            preEdit.putString(key, map.get(key));
        }
        preEdit.apply();
        return  0;
    }

    public static int delete(Context context, String preference, Set<String> set){
        SharedPreferences.Editor preEdit= context.getSharedPreferences(preference, Activity.MODE_PRIVATE).edit();
        for (String key : set){
            preEdit.remove(key);
        }
        preEdit.apply();
        return 0;
    }
}
