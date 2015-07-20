package com.dannybit.tuneflow.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnamdar on 7/4/15.
 */
public class Playlist {
    private long id;
    private ArrayList<Song> songs;
    private String name;
    private String artworkLink;

    public Playlist(String name){
        this.name = name;
        songs = new ArrayList<Song>();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void add(Song song){
        songs.add(song);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getSize(){
        return songs.size();
    }

}
