package com.limelight.utils;


import java.util.Map;

public abstract class LayoutProcessor {

    public abstract int init();

    public abstract Map<String, String> get();

    public abstract String get(String key);

    public abstract int add(String key, String value);

    public abstract int delete(String key);

    public abstract int update(String key, String newValue);

}
