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
        AppGlobals.sPreviousNotificationState = Helpers.getNotificationStreamVolume();
        Helpers.changeNotificationVolume(AppGlobals.DEFAULT_NOTIFICATION_VOLUME);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx) {
        Log.i("CallReceiver", "onIncomingCallEnded");
        Helpers.changeNotificationVolume(AppGlobals.sPreviousNotificationState);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx) {
        Log.i("CallReceiver", "onOutgoingCallStarted");
        AppGlobals.sPreviousNotificationState = Helpers.getNotificationStreamVolume();
        Helpers.changeNotificationVolume(AppGlobals.DEFAULT_NOTIFICATION_VOLUME);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx) {
        Log.i("CallReceiver", "onOutgoingCallEnded");
        Helpers.changeNotificationVolume(AppGlobals.sPreviousNotificationState);
    }

    @Override
    protected void onMissedCall(Context ctx) {

    }
}
