package com.coder520.mamabike.datastore;

import android.content.Context;
import android.content.SharedPreferences;

import com.coder520.mamabike.MamaApplication;

/**
 * Created by yadong on 2017/5/15.
 */

public class PreferenceDataSaver {
    private static final String PREFERENCE_NAME = "preference_data_store.xml";
    public static final String PREFERENCE_KEY_RODE_STARTED = "key_rode_start_time";
    public static final String PREFERENCE_KEY_USER_SESSION = "key_user_session";

    private Context mContext;

    public PreferenceDataSaver() {
        this.mContext = MamaApplication.getApplication();
    }

    private SharedPreferences getPreference() {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        getPreference().edit().putString(key, value).commit();
    }

    public void remove(String key) {
        getPreference().edit().remove(key).commit();
    }

    public String get(String key) {
        return getPreference().getString(key, null);
    }

    public void putLong(String key, Long value) {
        getPreference().edit().putLong(key, value).commit();
    }

    public Long getLong(String key) {
        return getPreference().getLong(key, 0L);
    }

    public void putBoolean(String key, Boolean value) {
        getPreference().edit().putBoolean(key, value).commit();
    }

    public Boolean getBoolean(String key) {
        return getPreference().getBoolean(key, false);
    }

}
