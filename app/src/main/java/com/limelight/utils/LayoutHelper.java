package com.limelight.utils;

import android.content.Context;
import java.util.regex.Pattern;


public class LayoutHelper {

    private static LayoutAdmin layoutAdmin;

    public static int selectLayout(String layoutName){

        return layoutAdmin.setCurrentLayoutNum(getLayoutList().indexOf(layoutName));
    }

    public static int selectLayout(int layoutIndex){

        return layoutAdmin.setCurrentLayoutNum(layoutIndex);
    }

    public static int addLayout(String layoutName){
        if (!Pattern.matches("^[A-Za-z0-9]{1,25}$",layoutName)){
            //名字非法
            return -2;
        }
        return layoutAdmin.addLayout(layoutName);

    }

    public static int renameCurrentLayout(String newLayoutName){
        if (!Pattern.matches("^[A-Za-z0-9]{1,25}$",newLayoutName)){
            //名字非法
            return -2;
        }
        return layoutAdmin.updateLayout(layoutAdmin.getCurrentLayoutNum(),newLayoutName);

    }

    public static int deleteCurrentLayout(){
        return layoutAdmin.deleteLayout(layoutAdmin.getCurrentLayoutNum());
    }


    public static int resetCurrentLayout(final Context context,final int layoutIndex){
        return 0;
    }

    public static void setLayoutAdmin(LayoutAdmin layoutAdmin){
        LayoutHelper.layoutAdmin = layoutAdmin;
    }

    public static LayoutList getLayoutListAdapterSelector(){
        return layoutAdmin.getLayoutList();
    }



    public static LayoutList getLayoutList(){
        return layoutAdmin.getLayoutList();
    }

    public static int getCurrentLayoutNum(){
        return layoutAdmin.getCurrentLayoutNum();
    }

    public static String getCurrentLayoutName(){
        return layoutAdmin.getLayoutList().get(layoutAdmin.getCurrentLayoutNum());
    }



    public static String getAdminName(){
        return layoutAdmin.toString();
    }
}
