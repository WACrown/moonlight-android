package com.limelight.utils;

import android.content.Context;

import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.preferences.StreamSettings;

import java.util.regex.Pattern;

public class LayoutEditHelper {

    public static int init(Context context){
        LayoutEdit layoutEdit = StreamSettings.SettingsFragment.getLayoutEdit(context,LayoutSelectHelper.getCurrentLayoutName(context));
        return layoutEdit.init();
    }

    public static int addButton(Context context, String name){
        LayoutEdit layoutEdit = StreamSettings.SettingsFragment.getLayoutEdit(context,LayoutSelectHelper.getCurrentLayoutName(context));
        return layoutEdit.add(name);
    }

    public static int deleteButton(Context context, String deleteName){
        LayoutEdit layoutEdit = StreamSettings.SettingsFragment.getLayoutEdit(context,LayoutSelectHelper.getCurrentLayoutName(context));
        return layoutEdit.delete(deleteName);
    }

    public static int resetAllButton(Context context){
        LayoutEdit layoutEdit = StreamSettings.SettingsFragment.getLayoutEdit(context,LayoutSelectHelper.getCurrentLayoutName(context));
        return layoutEdit.init();
    }

}
