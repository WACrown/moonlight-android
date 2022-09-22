package com.limelight.utils.controller;

public abstract class LayoutAdmin {

    public abstract LayoutList getLayoutList();

    public abstract int addLayout(String layoutName);

    public abstract int deleteLayout(int layoutNum);

    public abstract int updateLayout(int layoutNum, String layoutName);

    public abstract int getCurrentLayoutNum();

    public abstract int setCurrentLayoutNum(int currentNum);

    public abstract String toString();

}
