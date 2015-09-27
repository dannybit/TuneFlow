package com.dannybit.tuneflow.models;

import android.app.Notification;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.network.SoundcloudRestClient;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by danielnamdar on 7/18/15.
 */
public class SoundcloudSong extends Song {
    private String soundcloudId;

    public SoundcloudSong(){
        super();
        songType = SongType.SOUNDCLOUD;
    }

    public SoundcloudSong(Parcel in){
        super(in);
        songType = SongType.SOUNDCLOUD;
        soundcloudId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(soundcloudId);
    }

    public void setSoundcloudId(String soundcloudId){
        this.soundcloudId = soundcloudId;
    }

    public String getSoundcloudId(){
        return this.soundcloudId;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SoundcloudSong> CREATOR = new Parcelable.Creator<SoundcloudSong>() {
        @Override
        public SoundcloudSong createFromParcel(Parcel in) {
            return new SoundcloudSong(in);
        }

        @Override
        public SoundcloudSong[] newArray(int size) {
            return new SoundcloudSong[size];
        }
    };


    public String addClientIdToUrl(String url){
        return url + "?client_id=" + SoundcloudRestClient.CLIENT_ID;
    }

    public String getArtwork500x500(){
        if (getArtworkLink() == null) {
            return null;
        }
        return replaceLast(getArtworkLink(), "large", "t500x500");
    }

    private String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0) return string;
        String tail = string.substring(lastIndex).replaceFirst(from, to);
        return string.substring(0, lastIndex) + tail;
    }

    @Override
    public void loadImage(Context context, ImageView imageView) {
        if (getArtworkLink() != null && !getArtworkLink().equals("null")){
            Picasso.with(context).load(getArtwork500x500()).error(R.drawable.soundcloud_icon).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.soundcloud_icon).into(imageView);
        }
    }

    @Override
    public void loadThumbnail(Context context, ImageView imageView){
        if (getArtworkLink() != null && !getArtworkLink().equals("null")){
            Picasso.with(context).load(getArtworkLink()).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.soundcloud_icon).into(imageView);
        }

    }

    @Override
    public void loadNotificationArtwork(Context context, RemoteViews remoteViews, int viewId, int notificationId, Notification notification) {
        if (getArtworkLink() != null && !getArtworkLink().equals("null")){
            Picasso.with(context)
                    .load(getArtworkLink()).error(R.drawable.soundcloud_icon)
                    .into(remoteViews, viewId, notificationId, notification);
        } else {
            Picasso.with(context)
                    .load(R.drawable.soundcloud_icon)
                    .into(remoteViews, viewId, notificationId, notification);
        }

    }

    @Override
    public void styleTag(Context context, TextView tagText) {
        tagText.setText(R.string.soundcloud_tag_text);
        tagText.setTextColor(context.getResources().getColor(R.color.soundcloud_tag_color));
    }
}
