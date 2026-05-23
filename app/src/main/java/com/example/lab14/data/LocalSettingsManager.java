package com.example.lab14.data;

import android.content.Context;
import android.content.SharedPreferences;

public final class LocalSettingsManager {

    private static final String FILE_NAME = "user_settings";
    private static final String FIELD_ALIAS = "user_alias";
    private static final String FIELD_LOCALE = "app_locale";
    private static final String FIELD_VISUAL_MODE = "visual_mode";

    private LocalSettingsManager() {}

    public static boolean commitData(Context ctx, String alias, String locale, String mode, boolean isImmediate) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit()
                .putString(FIELD_ALIAS, alias)
                .putString(FIELD_LOCALE, locale)
                .putString(FIELD_VISUAL_MODE, mode);

        if (isImmediate) {
            return editor.commit();
        } else {
            editor.apply();
            return true;
        }
    }

    public static SettingData fetchSettings(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String alias = sp.getString(FIELD_ALIAS, "");
        String locale = sp.getString(FIELD_LOCALE, "fr");
        String mode = sp.getString(FIELD_VISUAL_MODE, "default");
        return new SettingData(alias, locale, mode);
    }

    public static void wipeSettings(Context ctx) {
        ctx.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static final class SettingData {
        public final String alias;
        public final String locale;
        public final String visualMode;

        public SettingData(String alias, String locale, String visualMode) {
            this.alias = alias;
            this.locale = locale;
            this.visualMode = visualMode;
        }
    }
}