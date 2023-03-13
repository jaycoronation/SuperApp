package com.application.alphacapital.superapp.acpital;

import android.content.Context;
import android.content.SharedPreferences;

public class CapitalPref {

    private static SharedPreferences sharedPrefs;
    static String PREF_NAME = "PREF";

    private static void getInstance(Context context) {
        if (sharedPrefs == null)
            sharedPrefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void putString(Context context, String key, String value) {
        getInstance(context);

        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.putString(key, value);
        edit.commit();

    }

    public static String getString(Context context, String key) {
        getInstance(context);

        String value = sharedPrefs.getString(key, null);

        return value;
    }

    public static void putBoolean(Context context, String key, boolean value) {

        getInstance(context);

        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.putBoolean(key, value);
        edit.commit();

    }

    public static boolean getBoolean(Context context, String key) {
        getInstance(context);

        boolean value = sharedPrefs.getBoolean(key, false);

        return value;
    }


    public static void putContactList(Context context, String key, String value) {

        getInstance(context);

        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.putString(key, value);
        edit.apply();

    }

    public static String getContactList(Context context, String key) {
        getInstance(context);

        String value = sharedPrefs.getString(key, null);

        return value;
    }
}
