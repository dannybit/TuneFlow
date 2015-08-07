package com.dannybit.tuneflow.models;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class Artist {

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
}
