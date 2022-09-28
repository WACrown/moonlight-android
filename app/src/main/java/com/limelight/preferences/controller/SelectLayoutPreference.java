package com.limelight.preferences.controller;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.limelight.utils.controller.LayoutAdminHelper;
import com.limelight.utils.controller.LayoutList;

public class SelectLayoutPreference extends ListPreference {
    public SelectLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LayoutAdminHelper.selectLayout(getContext(),(String) o);
                return true;
            }
        });
    }

    public SelectLayoutPreference(Context context) {
        super(context);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LayoutAdminHelper.selectLayout(getContext(),(String) o);
                return true;
            }
        });
    }

    @Override
    protected View onCreateDialogView() {
        ListView view = new ListView(getContext());
        view.setAdapter(adapter());
        LayoutList layoutList = LayoutAdminHelper.getLayoutList(getContext());
        CharSequence[] layoutCharSequences = layoutList.toArray(new CharSequence[layoutList.size()]);
        setEntries(layoutCharSequences);
        setEntryValues(layoutCharSequences);
        setValue(LayoutAdminHelper.getCurrentLayoutName(getContext()));
        return view;
    }

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }


}
