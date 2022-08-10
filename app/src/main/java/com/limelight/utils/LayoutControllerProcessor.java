package com.limelight.utils;

import android.content.Context;

import java.util.Map;

public class LayoutControllerProcessor extends LayoutProcessor{

    private Map<String, String> map;
    private final Context context;

    public LayoutControllerProcessor(Context context, String preference) {
        this.context = context;
        this.map = SharedPreferencesHelp.load(context, preference);
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
    public int add() {
        return 0;
    }

    @Override
    public int delete() {
        return 0;
    }

    @Override
    public int update() {
        return 0;
    }
}
