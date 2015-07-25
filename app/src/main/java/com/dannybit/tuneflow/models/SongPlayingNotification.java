package com.dannybit.tuneflow.models;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by danielnamdar on 7/25/15.
 */
public class SongPlayingNotification extends android.app.Notification {

    private Context context;
    private NotificationManager notificationManager;

    public SongPlayingNotification(Context context){
        super();
        this.context = context;
    }
}
