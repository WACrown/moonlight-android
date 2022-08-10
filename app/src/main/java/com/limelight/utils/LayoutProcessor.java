package com.limelight.utils;


import java.util.Map;

public abstract class LayoutProcessor {

    public abstract int init();

    public abstract Map<String, String> get();

    public abstract String get(String key);

    public abstract int add();

    public abstract int delete();

    public abstract int update();

}
