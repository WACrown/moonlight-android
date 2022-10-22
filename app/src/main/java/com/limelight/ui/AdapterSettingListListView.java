package com.limelight.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.limelight.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSettingListListView extends BaseAdapter {

    private final Context context;
    private List<String> itemList = new ArrayList<>();

    public AdapterSettingListListView(Context context) {
        this.context = context;
    }

    public void setItemList(List<String> itemList){
        this.itemList = itemList;
    }

    public List<String> getItemList() {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.game_setting_list_listview_item, null);
        TextView gameSettingItemView = view.findViewById(R.id.game_setting_item_view);
        gameSettingItemView.setText((String) getItem(i));
        return view;
    }
}
