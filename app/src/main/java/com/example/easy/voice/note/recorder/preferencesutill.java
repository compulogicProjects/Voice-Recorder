package com.example.easy.voice.note.recorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;


public class preferencesutill {
    private static final String TAG = preferencesutill.class.getSimpleName();


    public static void setPreviousSongInSharedPrefs(Context context, Uri songName, String song) {
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
            sharedPrefsEditor.putString("PREVIOUS_SONG" , String.valueOf(songName));
            sharedPrefsEditor.putString("song",song);
            sharedPrefsEditor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* get store previous song*/
    public static String getPreviousSongFromSharedPrefs(Context context) {
        String previousSong = "";
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            previousSong = sharedPrefs.getString("PREVIOUS_SONG" , null);
            String songname=sharedPrefs.getString("song",null);
            return previousSong;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return previousSong;
    }
}
