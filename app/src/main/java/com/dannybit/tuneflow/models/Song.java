package com.dannybit.tuneflow.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.dannybit.tuneflow.views.adapters.SongAdapter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by danielnamdar on 6/27/15.
 */
public abstract class Song implements Parcelable {

    private long id;
    private String trackName;
    private String duration;
    private String artworkLink;
    private String url;

    public Song() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        trackName = in.readString();
        duration = in.readString();
        artworkLink = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(trackName);
        dest.writeString(duration);
        dest.writeString(artworkLink);
        dest.writeString(url);
    }


    public abstract void loadImage(Context context, ImageView imageView);
    public abstract void loadThumbnail(Context context, ImageView imageView);


    @Override
    public String toString() {
        return getTrackName();
    }
}