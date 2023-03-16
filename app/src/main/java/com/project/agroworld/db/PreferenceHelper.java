package com.project.agroworld.db;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static PreferenceHelper yourPreference;
    private final SharedPreferences sharedPreferences;

    public static PreferenceHelper getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new PreferenceHelper(context);
        }
        return yourPreference;
    }

    private PreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("prep_helper", Context.MODE_PRIVATE);
    }

    public void saveData(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public boolean getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }
}