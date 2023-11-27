package com.ftn.slagalica.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ftn.slagalica.R;

public interface IThemeHandler {
    /** Reads user-saved Theme & sets it for the calling Activity.
     * Light theme is set by default.
     * @param thisActivity Activity
     * @return darkThemeOn boolean
     */
    default boolean setupTheme(Activity thisActivity){
        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences("theme", Context.MODE_PRIVATE);
        boolean darkThemeOn = sharedPreferences.getBoolean("dark", false);
        if (darkThemeOn)
            thisActivity.setTheme(R.style.Theme_Slagalica_MyDark);
        return darkThemeOn;
    }
}
