package com.jecelyin.editor.v2.ui.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import com.jecelyin.editor.v2.Pref;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.preference.JecListPreference;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            if (value == null)
                return true;
            String stringValue = value.toString();
            String key = preference.getKey();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else if (preference instanceof CheckBoxPreference) {
                ((CheckBoxPreference) preference).setChecked((boolean) value);
            } else if("pref_highlight_file_size_limit".equals(key)) {
                preference.setSummary(stringValue + " KB");
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(String.valueOf(stringValue));
            }

            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        dependBindPreference(getPreferenceScreen());
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    private static void dependBindPreference(PreferenceGroup pg) {
        int count = pg.getPreferenceCount();
        Preference preference;
        String key;
        Object value;

        Pref pref = Pref.getInstance(pg.getContext());

        for(int i = 0; i < count; i++) {
            preference = pg.getPreference(i);
            key = preference.getKey();

            if(preference instanceof PreferenceGroup) {
                dependBindPreference((PreferenceGroup) preference);
                continue;
            }

            Class<? extends Preference> cls = preference.getClass();
            if(cls.equals(Preference.class))
                continue;

            value = pref.getValue(key);

            if(preference instanceof JecListPreference) {
//                if("pref_font_size".equals(key)) {
//                    new FontSizePreference((JecListPreference)preference);
//                } else if("pref_cursor_width".equals(key)) {
//                    new CursorWidthPreference((JecListPreference)preference);
//                }
            } else if(preference instanceof EditTextPreference) {
                ((EditTextPreference)preference).setText(String.valueOf(value));
            } else if(preference instanceof CheckBoxPreference) {
                ((CheckBoxPreference)preference).setChecked((boolean)value);
            }

            if (!Pref.KEY_SYMBOL.equals(key))
                bindPreferenceSummaryToValue(preference);
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        String key = preference.getKey();
        Object value = Pref.getInstance(preference.getContext()).getValue(key);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, value);
    }

}
