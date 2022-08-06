package com.limelight.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.limelight.R;
import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.LayoutList;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class AdapterSelector implements SpinnerAdapter {

    private Context context;
    private LayoutList list = null;

    public AdapterSelector(Context context) {
        this.context = context;
        if (PreferenceConfiguration.readPreferences(context).onscreenController){
            list = SelectControllerLayoutHelp.loadAllLayoutNameShow(context);
        } else if (PreferenceConfiguration.readPreferences(context).onscreenKeyboard) {
            list = SelectKeyboardLayoutHelp.loadAllLayoutNameShow(context);
        }


    }




    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.spinner_getview, null);
        TextView tvgetView = view.findViewById(R.id.tvgetView);
        tvgetView.setText(getItem(i).toString());
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.spinner_getview, null);
        TextView tvgetView = view.findViewById(R.id.tvgetView);
        tvgetView.setText(getItem(i).toString());
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
