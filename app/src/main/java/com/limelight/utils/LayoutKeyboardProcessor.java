package com.limelight.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import java.util.Map;

public class LayoutKeyboardProcessor extends LayoutProcessor{
    private final String preference;
    private Map<String, String> map;
    private final Context context;

    public LayoutKeyboardProcessor(Context context, String preference) {
        this.context = context;
        this.preference = preference;
        this.map = SharedPreferencesHelp.load(context, preference);
        init();
    }

    @Override
    public int init() {
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
    public int add(String key, String value) {

        if (map.containsKey(key)){
            return -1;
        }
        map.put(key, value);
        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }

    @Override
    public int delete(String key) {
        if (!map.containsKey(key)){
            return -1;
        }
        map.remove(key);
        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public int update(String key, String newValue) {

        if (!map.containsKey(key)){
            return -1;
        }
        map.replace(key, newValue);
        SharedPreferencesHelp.store(context, preference, map);
        return 0;
    }
}
