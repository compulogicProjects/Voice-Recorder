package com.easy.voice.note.recorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;


public class preferencesutill {
    private static final String TAG = preferencesutill.class.getSimpleName();


    public static void setPreviousSongInSharedPrefs(Context context, String songUri,
                                                    int seekprogres,boolean pref) {
        try {
            SharedPreferences sharedPrefs = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
            sharedPrefsEditor.putString("PREVIOUS_SONG" , songUri);
            sharedPrefsEditor.putInt("seekprogress",seekprogres);
            sharedPrefsEditor.putBoolean("pref",pref);
            sharedPrefsEditor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* get store previous song*/
    public static String getPreviousSongFromSharedPrefs(Context context) {
        String pre = "";
        try {
            SharedPreferences sharedPrefs = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
            pre=sharedPrefs.getString("PREVIOUS_SONG",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pre;
    }
    public static int getseekprogressFromSharedPrefs(Context context) {
        int seekprogress = 0;
        try {
            SharedPreferences sharedPrefs = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
            seekprogress=sharedPrefs.getInt("seekprogress",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seekprogress;
    }
    public static boolean getpref(Context context){
        boolean pref=true;
        try {
            SharedPreferences sharedPrefs = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
            pref=sharedPrefs.getBoolean("pref",false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pref;
    }
    public static String getsongname(Context context) {
        String pre = "";
        try {
            SharedPreferences sharedPrefs = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
            pre=sharedPrefs.getString("song",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pre;
    }
    public static void clearpref(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
