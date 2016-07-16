package com.taxiapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class PreferencesUtil {

    public static final String USER = "userDetails";
    public static final String FAV_ADDRESSES = "favAddresses";
    public static final String LAST_LOC = "lastLocation";
    public static final String BASE_URL = "apiURL";
    public static final String APP_UPDATE_RESULT = "updaterResult";

    private static SharedPreferences getDefaultPrefs(Context c) {

        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static <T> T get(Context context, String key, Class<T> clazz) {
        SharedPreferences prefs = getDefaultPrefs(context);
        String value = prefs.getString(key, null);
        if (value == null) {
            return null;
        }
        T t = new Gson().fromJson(value, clazz);
        return t;
    }

    public static <T> T get(Context context, String key, Type type) {
        SharedPreferences prefs = getDefaultPrefs(context);
        String value = prefs.getString(key, null);
        if (value == null) {
            return null;
        }
        T t = new Gson().fromJson(value, type);
        return t;
    }

    public static <T> boolean put(Context context, String key, T obj) {
        SharedPreferences prefs = getDefaultPrefs(context);
        String value = new Gson().toJson(obj);
        return prefs.edit().putString(key, value).commit();
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences prefs = getDefaultPrefs(context);
        return prefs.contains(key);
    }

    public static long getLong(Context ctx, String key, int defaultValue) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        return prefs.getLong(key, defaultValue);
    }

    public static boolean putLong(Context ctx, String key, long value) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        return prefs.edit().putLong(key, value).commit();
    }

    public static void remove(Context ctx, String key) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        prefs.edit().remove(key).commit();
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        return prefs.getString(key, defaultValue);
    }

    public static boolean putString(Context ctx, String key, String value) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        return prefs.edit().putString(key, value).commit();
    }


    public static boolean getBool(Context ctx, String key, boolean defaultValue) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        return prefs.getBoolean(key, defaultValue);
    }

    public static boolean putBool(Context ctx, String key, boolean value) {
        SharedPreferences prefs = getDefaultPrefs(ctx);
        return prefs.edit().putBoolean(key, value).commit();
    }

}
