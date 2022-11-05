package com.limelight.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterSettingMenuListView extends BaseAdapter {

    private final Context context;
    private List<MenuItem> items = new ArrayList<>();

    public AdapterSettingMenuListView(Context context) {
        this.context = context;
    }

    public void setItemList(List<MenuItem> items){
        this.items = items;
    }

    public List<MenuItem> getItemList() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
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
