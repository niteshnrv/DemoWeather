package com.example.niteshverma.demoweather.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_KEY_FIRST_HELP_SHOWN = "PREF_KEY_FIRST_HELP_SHOWN";
    private static PreferenceManager preferenceManager;

    private PreferenceManager() {
    }

    public static PreferenceManager getInstance() {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager();
        }
        return preferenceManager;
    }

    private static final String ER_PREFS_NAME = "ER_PREFERENCE";

    public boolean isHelpDialogFirstTime(Context context) {
        boolean isFirstTIme = false;
        try {
            SharedPreferences prefs = context.getSharedPreferences(ER_PREFS_NAME, Context.MODE_PRIVATE);
            isFirstTIme = prefs.getBoolean(PREF_KEY_FIRST_HELP_SHOWN, true);
        } catch (Exception e) {
            Utilities.printStackTrace(e);
        }
        return isFirstTIme;
    }

    public void setIsFirstTImeHelp(Context context, boolean isFirstTime) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(ER_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putBoolean(PREF_KEY_FIRST_HELP_SHOWN, isFirstTime);
            editor.commit();
        } catch (Exception e) {
            Utilities.printStackTrace(e);
        }
    }

}
