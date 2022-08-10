package com.limelight.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.LayoutHelper;
import com.limelight.utils.LayoutList;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class SelectLayoutPreference extends ListPreference {
    public SelectLayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LayoutHelper.selectLayout((String) o);
                return true;
            }
        });
    }

    public SelectLayoutPreference(Context context) {
        super(context);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LayoutHelper.selectLayout((String) o);
                return true;
            }
        });
    }

    @Override
    protected View onCreateDialogView() {
        ListView view = new ListView(getContext());
        view.setAdapter(adapter());
        LayoutList layoutList = LayoutHelper.getLayoutList();
        CharSequence[] layoutCharSequences = layoutList.toArray(new CharSequence[layoutList.size()]);
        setEntries(layoutCharSequences);
        setEntryValues(layoutCharSequences);
        setValue(LayoutHelper.getCurrentLayoutName());
        return view;
    }

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }


}
