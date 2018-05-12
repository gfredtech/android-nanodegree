package me.gfred.bakingapp.fragment;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.List;

import me.gfred.bakingapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);

        ListPreference preference = (ListPreference) findPreference("ingredient");
        setEntries(preference);
    }

    static void setEntries(ListPreference listPreference) {
        CharSequence[] entries = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};
        CharSequence[] entryValues = {"1", "2", "3", "4"};
        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);
    }
}
