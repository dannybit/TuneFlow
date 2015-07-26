package com.dannybit.tuneflow.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dannybit.tuneflow.activities.NowPlayingActivity;

public class LaunchNowPlayingBroadcastReceiver extends BroadcastReceiver {
    public LaunchNowPlayingBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, NowPlayingActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
