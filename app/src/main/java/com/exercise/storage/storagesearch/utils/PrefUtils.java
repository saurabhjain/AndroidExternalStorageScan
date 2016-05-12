package com.exercise.storage.storagesearch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.exercise.storage.storagesearch.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjain70 on 5/11/16.
 */
public class PrefUtils {

    private static final String ITEM = "Item";

    public static void storeItems(Context context, ArrayList<Item> fileItems) {
        clearPrefs(context);
        SharedPreferences preferences = getPreferences(context);
        Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fileItems);
        prefsEditor.putString(ITEM, json);
        prefsEditor.commit();
    }

    public static List<Item> getItems(Context context) {
        SharedPreferences preferences = getPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(ITEM, "");
        Type type = new TypeToken<List<Item>>(){}.getType();
        List<Item> fileItems = gson.fromJson(json, type);
        return fileItems;
    }

    private static final SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void clearPrefs(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }
}
