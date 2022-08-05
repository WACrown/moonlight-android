package com.limelight.preferences.Controller;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.limelight.utils.LayoutList;
import com.limelight.utils.SelectControllerLayoutHelp;

public class DynamicListPreference extends ListPreference {
    public DynamicListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicListPreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateDialogView() {
        ListView view = new ListView(getContext());
        view.setAdapter(adapter());
        LayoutList layoutList = SelectControllerLayoutHelp.loadAllLayoutName(getContext());
        CharSequence[] layoutCharSequences = layoutList.toArray(new CharSequence[layoutList.size()]);
        setEntries(layoutCharSequences);
        setEntryValues(layoutCharSequences);
        setValue(SelectControllerLayoutHelp.loadSingleLayoutName(getContext(), SelectControllerLayoutHelp.getCurrentNum(getContext())));
        return view;
    }

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }



}