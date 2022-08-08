package com.limelight.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.LayoutList;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

public class DynamicListPreference extends ListPreference {
    public DynamicListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicListPreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateDialogView() {
        if (PreferenceConfiguration.readPreferences(getContext()).onscreenController){
            ListView view = new ListView(getContext());
            view.setAdapter(adapter());
            LayoutList layoutListKey = SelectControllerLayoutHelp.loadAllLayoutNameShow(getContext());
            LayoutList layoutListValue = SelectControllerLayoutHelp.loadAllLayoutName(getContext());
            CharSequence[] layoutCharSequencesKey = layoutListKey.toArray(new CharSequence[layoutListKey.size()]);
            CharSequence[] layoutCharSequencesValue = layoutListValue.toArray(new CharSequence[layoutListValue.size()]);
            setEntries(layoutCharSequencesKey);
            setEntryValues(layoutCharSequencesValue);
            setValue(SelectControllerLayoutHelp.loadSingleLayoutName(getContext(), SelectControllerLayoutHelp.getCurrentController(getContext())));
            return view;
        } else if (PreferenceConfiguration.readPreferences(getContext()).onscreenKeyboard) {
            ListView view = new ListView(getContext());
            view.setAdapter(adapter());
            LayoutList layoutListKey = SelectKeyboardLayoutHelp.loadAllLayoutNameShow(getContext());
            LayoutList layoutListValue = SelectKeyboardLayoutHelp.loadAllLayoutName(getContext());
            CharSequence[] layoutCharSequencesKey = layoutListKey.toArray(new CharSequence[layoutListKey.size()]);
            CharSequence[] layoutCharSequencesValue = layoutListValue.toArray(new CharSequence[layoutListValue.size()]);
            setEntries(layoutCharSequencesKey);
            setEntryValues(layoutCharSequencesValue);
            setValue(SelectKeyboardLayoutHelp.loadSingleLayoutName(getContext(), SelectKeyboardLayoutHelp.getCurrentController(getContext())));
            return view;
        }
        return null;
    }

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }



}
