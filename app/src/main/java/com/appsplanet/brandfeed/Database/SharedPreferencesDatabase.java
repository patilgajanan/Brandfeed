package com.appsplanet.brandfeed.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;

import com.appsplanet.brandfeed.Config.StringConfig;


/**
 * Created by tejas nitore on 09/05/2017.
 */

public class SharedPreferencesDatabase {
    private Context context;
    private SharedPreferences sharedpreferences;

    public SharedPreferencesDatabase(Context context) {
        this.context = context;
    }
    private Bundle mExtras;
    public void createDatabase() {
        sharedpreferences = context.getSharedPreferences(StringConfig.SHARED_PREFERENCE_DB_NAME, Context.MODE_PRIVATE);
    }

    public void addDataPrivilege(String key, String[] values) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for (int i = 0; i < values.length; i++) {
            editor.putString(key + "_" + i, values[i]);
        }
        editor.commit();
    }

    public void addData(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public String[] getDataPrivilege(String key, int count) {
        String s[] = {};
        for (int i = 0; i < count; i++) {
            s[i] = sharedpreferences.getString(key + "_" + i, null);
        }
        return s;
    }

    public String getData(String key) {
        return sharedpreferences.getString(key, null);
    }

    public void removeData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void removeDataByKey(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
