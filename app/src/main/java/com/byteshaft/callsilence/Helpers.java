package com.byteshaft.callsilence;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Created by s9iper1 on 11/13/16.
 */

public class Helpers {

    private static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(AppGlobals.getContext());
    }

    public static void saveButtonState(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(AppGlobals.sButtonState, value).apply();
    }

    public static boolean getButtonState() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(AppGlobals.sButtonState, false);
    }

    private static void changeVolume(int value) {
        AudioManager audioManager =
                (AudioManager) AppGlobals.getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != value) {
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, value, 0);
        }
    }

    public static void changeNotificationVolume(int value) {
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            changeVolume(value);
            //}
        } else {
            changeVolume(value);
        }
    }

    public static int getNotificationStreamVolume() {
        AudioManager am =
                (AudioManager) AppGlobals.getContext().getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
    }
}
