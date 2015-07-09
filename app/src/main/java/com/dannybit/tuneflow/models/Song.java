package com.dannybit.tuneflow.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by danielnamdar on 6/27/15.
 */
public class Song implements Parcelable {
    private String trackId;
    private String trackName;
    private String duration;
    private String artworkLink;
    private String url;

    public Song() {

    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getDurationInMins(){
        int durationInInt = Integer.parseInt(duration);
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(durationInInt),
                TimeUnit.MILLISECONDS.toSeconds(durationInInt) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationInInt))
        );
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
        trackId = in.readString();
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
        dest.writeString(trackId);
        dest.writeString(trackName);
        dest.writeString(duration);
        dest.writeString(artworkLink);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}