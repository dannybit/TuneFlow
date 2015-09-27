package com.dannybit.tuneflow.models;

import android.app.Notification;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danielnamdar on 6/27/15.
 */
public abstract class Song implements Parcelable {

    private long id;
    private String artist;
    private String trackName;
    private String duration;
    private String artworkLink;
    private String url;
    protected SongType songType;

    public Song() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getDurationInMins(){
        int durationInInt = Integer.parseInt(duration);
        return new SimpleDateFormat("mm:ss").format(new Date(durationInInt));
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setArtworkLink(String artworkLink) {
        this.artworkLink = artworkLink;
    }

    public String getArtworkLink() {
        return artworkLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        artist = in.readString();
        trackName = in.readString();
        duration = in.readString();
        artworkLink = in.readString();
        url = in.readString();
        songType = (SongType) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(artist);
        dest.writeString(trackName);
        dest.writeString(duration);
        dest.writeString(artworkLink);
        dest.writeString(url);
        dest.writeSerializable(songType);
    }


    public abstract void loadImage(Context context, ImageView imageView);
    public abstract void loadThumbnail(Context context, ImageView imageView);
    public abstract void loadNotificationArtwork(Context context, RemoteViews remoteViews, int viewId, int notificationId, Notification notification);
    public abstract void styleTag(Context context, TextView tagText);

    public SongType getSongType(){
        return this.songType;
    }


    @Override
    public String toString() {
        return getTrackName();
    }
}