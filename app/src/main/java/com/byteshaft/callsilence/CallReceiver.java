package com.byteshaft.callsilence;

import android.content.Context;
import android.util.Log;

/**
 * Created by s9iper1 on 11/12/16.
 */

public class CallReceiver extends Receiver {
    @Override
    protected void onIncomingCallReceived(Context ctx) {
        Log.i("CallReceiver", "onIncomingCallReceived");
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx) {
        Log.i("CallReceiver", "onIncomingCallAnswered");
        Log.i("Before Change", "notification volume "+ Helpers.getNotificationStreamVolume());
        AppGlobals.sPreviousNotificationState = Helpers.getNotificationStreamVolume();
        AppGlobals.sPreviousRingerMode = Helpers.getRingerMode();
        Helpers.changeNotificationVolume(AppGlobals.DEFAULT_NOTIFICATION_VOLUME);
        Log.i("After Changed", "notification volume "+ Helpers.getNotificationStreamVolume());
    }

    @Override
    protected void onIncomingCallEnded(Context ctx) {
        Log.i("onIncomingCallEnded", " MODE : "+ AppGlobals.sPreviousRingerMode);
        Helpers.changeNotificationVolume(AppGlobals.sPreviousNotificationState);
        Helpers.setBackRingerMode(AppGlobals.sPreviousRingerMode);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx) {
        Log.i("CallReceiver", "onOutgoingCallStarted");
        AppGlobals.sPreviousNotificationState = Helpers.getNotificationStreamVolume();
        AppGlobals.sPreviousRingerMode = Helpers.getRingerMode();
        Helpers.changeNotificationVolume(AppGlobals.DEFAULT_NOTIFICATION_VOLUME);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx) {
        Log.i("CallReceiver", "onOutgoingCallEnded");
        Log.i("onOutgoingCallEnded", " MODE : "+ AppGlobals.sPreviousRingerMode);
        Helpers.changeNotificationVolume(AppGlobals.sPreviousNotificationState);
        Helpers.setBackRingerMode(AppGlobals.sPreviousRingerMode);
    }

    @Override
    protected void onMissedCall(Context ctx) {

    }
}
