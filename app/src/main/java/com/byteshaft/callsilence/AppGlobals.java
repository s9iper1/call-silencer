package com.byteshaft.callsilence;

import android.app.Application;
import android.content.Context;

/**
 * Created by s9iper1 on 11/12/16.
 */

public class AppGlobals extends Application {

    private static Context sContext;
    public static int sPreviousNotificationState = 3;
    public static String sButtonState = "button_state";
    public static final int DEFAULT_NOTIFICATION_VOLUME = 0;
    public static int sPreviousRingerMode = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
