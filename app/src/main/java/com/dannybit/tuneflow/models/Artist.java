package com.dannybit.tuneflow.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class Artist implements Parcelable {

    private long id;
    private String artistName;
    private int albumCount;
    private int songCount;

    public Artist(long id, String artistName, int albumCount, int songCount) {
        this.id = id;
        this.artistName = artistName;
        this.albumCount = albumCount;
        this.songCount = songCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        artistName = in.readString();
        albumCount = in.readInt();
        songCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(artistName);
        dest.writeInt(albumCount);
        dest.writeInt(songCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
