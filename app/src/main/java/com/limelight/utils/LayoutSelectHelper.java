package com.limelight.utils;

import android.content.Context;

import com.limelight.preferences.StreamSettings;


public class LayoutSelectHelper {

    public static int selectLayout(Context context,String layoutName){

        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.setCurrentLayoutNum(getLayoutList(context).indexOf(layoutName));

    }

    public static int selectLayout(Context context,int layoutIndex){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.setCurrentLayoutNum(layoutIndex);
    }




    public static LayoutList getLayoutList(Context context){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.getLayoutList();
    }

    public static int getCurrentLayoutNum(Context context){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.getCurrentLayoutNum();
    }

    public static String getCurrentLayoutName(Context context){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.getLayoutList().get(layoutAdmin.getCurrentLayoutNum());
    }



    public String getAdminName(Context context){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.toString();
    }
}
