package com.limelight.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterSettingMenuListView extends BaseAdapter {

    private final Context context;
    private List<MenuItemLinearLayout> itemList = new ArrayList<>();

    public AdapterSettingMenuListView(Context context) {
        this.context = context;
    }

    public void setItemList(List<MenuItemLinearLayout> itemList){
        this.itemList = itemList;
    }

    public List<MenuItemLinearLayout> getItemList() {
        return itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = (View) getItem(position);
        return convertView;
    }
}
