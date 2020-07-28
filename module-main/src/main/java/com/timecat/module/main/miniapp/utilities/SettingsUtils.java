package com.timecat.module.main.miniapp.utilities;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.timecat.component.setting.DEF;


public class SettingsUtils {
    public static void SetValue(Context context, String key, String value) {
        Editor editor = DEF.floatview().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String GetValue(Context context, String key) {
        return DEF.floatview().getString(key, "");
    }
}
