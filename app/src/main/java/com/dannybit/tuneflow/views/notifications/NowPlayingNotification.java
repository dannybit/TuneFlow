package com.dannybit.tuneflow.views.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.dannybit.tuneflow.activities.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;
import com.squareup.picasso.Picasso;

/**
 * Created by danielnamdar on 7/26/15.
 */
public class NowPlayingNotification  {

    private Context context;

    public NowPlayingNotification(Context context){
        this.context = context;
    }

    public Notification createNowPlayingNotification(Song song, boolean songPlaying){
        RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(), R.layout.notification_view);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setWhen(System.currentTimeMillis())
                .setContent(remoteViews);

        remoteViews.setTextViewText(R.id.notification_song_name, song.getTrackName());

        if (songPlaying){
            remoteViews.setImageViewResource(R.id.bNotificationPlayOrPause, R.drawable.btn_pause);
        } else {
            remoteViews.setImageViewResource(R.id.bNotificationPlayOrPause, R.drawable.btn_play);
        }

        Intent launchNowPlayingIntent = new Intent();
        launchNowPlayingIntent.setAction(MainActivity.LAUNCH_NOW_PLAYING_ACTION);
        PendingIntent launchNowPlayingPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, launchNowPlayingIntent, 0);
        builder.setContentIntent(launchNowPlayingPendingIntent);


        Intent playPauseTrackIntent = new Intent();
        playPauseTrackIntent.setAction(MainActivity.PLAY_PAUSE_ACTION);
        PendingIntent playPauseTrackPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, playPauseTrackIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.bNotificationPlayOrPause, playPauseTrackPendingIntent);

        Intent backwardTrackIntent = new Intent();
        backwardTrackIntent.setAction(MainActivity.BACKWARD_ACTION);
        PendingIntent backwardTrackPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, backwardTrackIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.bNotificationBackward, backwardTrackPendingIntent);

        Notification notification = builder.getNotification();
        // Bug in NotificationCompat that does not set the content.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            notification.contentView = remoteViews;
        }

        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(MainActivity.PLAYING_SONG_NOTIFICATION_ID, notification);

        song.loadNotificationArtwork(context, remoteViews, R.id.notification_artwork, MainActivity.PLAYING_SONG_NOTIFICATION_ID, notification);

        return notification;
    }
}
