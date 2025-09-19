package com.example.breaker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class Utils {

    public static void focusView(View v) {
        v.requestFocus();
        getIMM(v).showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(View v) {
        getIMM(v).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void showToast(Context context, int textResId) {
        Toast.makeText(context, textResId, Toast.LENGTH_SHORT).show();
    }

    public static void animateAlpha(View v, float alpha) {
        v.animate().alpha(alpha).setDuration(Config.animationDuration / 2).start();
    }

    private static InputMethodManager getIMM(View v) {
        Activity activity = (Activity) v.getContext();
        return (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    }

    private static SharedPreferences getPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Config.preferencesFilename, Context.MODE_PRIVATE);
        return preferences;
    }

    public static void setPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearPreference(Context context, String key) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getPreference(Context context, String key) {
        return getPreferences(context).getString(key, null);
    }

    public static String getPreference(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }

    public static boolean isUsernamePreferencesSet(Context context) {
        return getPreferences(context).contains(Config.preferencesUsername);
    }

    public static boolean isPreferencesSet(Context context) {
        return getPreferences(context).contains(Config.preferencesTheme) &&
                getPreferences(context).contains(Config.preferencesLanguage);
    }

    public static String getPreferencesUsername(Context context) {
        return getPreference(context, Config.preferencesUsername);
    }

    public static String getPreferencesUsernameOrDefault(Context context) {
        return getPreference(context, Config.preferencesUsername, context.getString(R.string.default_username));
    }

    public static String getPreferencesTheme(Context context) {
        return getPreference(context, Config.preferencesTheme);
    }

    public static String getPreferencesThemeOrDefault(Context context) {
        return getPreference(context, Config.preferencesTheme, Config.defaultTheme);
    }

    public static String getPreferencesLanguage(Context context) {
        return getPreference(context, Config.preferencesLanguage);
    }

    public static String getPreferencesLanguageOrDefault(Context context) {
        return getPreference(context, Config.preferencesLanguage, Config.defaultLanguage);
    }

    public static void setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static void setSelectedTheme(Context context) {
        if (getPreferencesThemeOrDefault(context).equals(Config.preferencesThemeDark)) {
            setDarkTheme();
        } else {
            setLightTheme();
        }
    }

    public static void setLanguage(Activity activity, String languageCode, boolean recreate) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        activity.getResources().updateConfiguration(config,
                activity.getResources().getDisplayMetrics());

        if (recreate) {
            activity.recreate();
        }
    }

    public static void setSelectedLanguage(Activity activity) {
        setLanguage(activity, getPreferencesLanguageOrDefault(activity), false);
    }

    private Utils() {};
}
