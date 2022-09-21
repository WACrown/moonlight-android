package com.limelight.utils;

import android.content.Context;

import com.limelight.preferences.StreamSettings;

import java.util.regex.Pattern;

public class LayoutAdminHelper {

    public static int addLayout(Context context,String layoutName){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.addLayout(layoutName);

    }

    public static int renameCurrentLayout(Context context, String newLayoutName){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.updateLayout(layoutAdmin.getCurrentLayoutNum(),newLayoutName);

    }

    public static int deleteCurrentLayout(Context context){
        LayoutAdmin layoutAdmin = StreamSettings.SettingsFragment.getLayoutAdmin(context);
        return layoutAdmin.deleteLayout(layoutAdmin.getCurrentLayoutNum());
    }

}
