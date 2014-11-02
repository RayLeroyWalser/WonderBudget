package com.android.rubeus.wonderbudget.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.rubeus.wonderbudget.MainActivity;

/**
 * Created by rubeus on 11/2/14.
 */
public class PreferencesUtility {

    public static int getAccount(Context context){
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREF, 0);
        return settings.getInt("account", 1);
    }

    public static void saveAccount(Context context, int account){
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("account", account);
        editor.commit();
    }
}