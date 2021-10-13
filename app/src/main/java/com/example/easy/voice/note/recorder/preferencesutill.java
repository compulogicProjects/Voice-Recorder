package com.example.easy.voice.note.recorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;


public class preferencesutill {
    private static final String TAG = preferencesutill.class.getSimpleName();


    public static void setPreviousSongInSharedPrefs(Context context, Uri songName, int seekprogres) {
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
            sharedPrefsEditor.putString("PREVIOUS_SONG" , String.valueOf(songName));
            sharedPrefsEditor.putInt("seekprogress",seekprogres);
            sharedPrefsEditor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* get store previous song*/
    public static Uri getPreviousSongFromSharedPrefs(Context context) {
        Uri previoussong = null;
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            previoussong=Uri.parse(sharedPrefs.getString("PREVIOUS_SONG",null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return previoussong;
    }
    public static int getseekprogressFromSharedPrefs(Context context) {
        int seekprogress = 0;
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            seekprogress=sharedPrefs.getInt("seekprogress",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seekprogress;
    }
}
