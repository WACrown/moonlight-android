package com.limelight.utils.controller;

import java.util.ArrayList;
import java.util.Collections;

public class LayoutList extends ArrayList<String> {


    public int StringToList(String listString){
        if (listString == null){
            return 1;
        } else {
            String[] layouts = listString.split(",");
            Collections.addAll(this, layouts);
            return 0;
        }
    }

    @Override
    public String toString() {
        String layoutList = "";
        for (String layoutName : this){
            layoutList = layoutList + layoutName + ",";
        }
        layoutList = layoutList.substring(0,layoutList.length() - 1);
        return layoutList;
    }
}
