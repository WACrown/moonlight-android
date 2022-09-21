package com.limelight.preferences.controller;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.limelight.utils.LayoutSelectHelper;
import com.limelight.utils.LayoutList;

public class SelectLayoutPreference extends ListPreference {
    public SelectLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LayoutSelectHelper.selectLayout(getContext(),(String) o);
                return true;
            }
        });
    }

    public SelectLayoutPreference(Context context) {
        super(context);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LayoutSelectHelper.selectLayout(getContext(),(String) o);
                return true;
            }
        });
    }

    @Override
    protected View onCreateDialogView() {
        ListView view = new ListView(getContext());
        view.setAdapter(adapter());
        LayoutList layoutList = LayoutSelectHelper.getLayoutList(getContext());
        CharSequence[] layoutCharSequences = layoutList.toArray(new CharSequence[layoutList.size()]);
        setEntries(layoutCharSequences);
        setEntryValues(layoutCharSequences);
        setValue(LayoutSelectHelper.getCurrentLayoutName(getContext()));
        return view;
    }

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }


}
