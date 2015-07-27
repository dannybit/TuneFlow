package com.dannybit.tuneflow.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dannybit.tuneflow.activities.MainActivity;

public class PlayPauseBroadcastReceiver extends BroadcastReceiver {
    public PlayPauseBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.instance.togglePlayOrPause();
    }
}
