package com.limelight.utils.controller;


import java.util.Map;

public abstract class LayoutEdit {

    public abstract int init();

    public abstract Map<String, String> get();

    public abstract String get(String key);

    public abstract int add(String key);

    public abstract int delete(String key);

    public abstract int update(String key, String newValue);

}
