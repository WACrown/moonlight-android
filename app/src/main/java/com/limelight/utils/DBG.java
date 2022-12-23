package com.limelight.utils;

public class DBG {

    private static final boolean DBG_MODE = true;
    private static final String PRE_DBG = "wangguan";

    public static void print(String text){
        if (DBG_MODE){
            System.out.println(PRE_DBG + "," + text);
        }
    }
}
