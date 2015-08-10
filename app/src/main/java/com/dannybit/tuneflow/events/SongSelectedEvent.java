package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 8/10/15.
 */
public class SongSelectedEvent {

    private ArrayList<Song> songs;
    private int position;

    public SongSelectedEvent(ArrayList<Song> songs, int position){
        this.songs = songs;
        this.position = position;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getPosition() {
        return position;
    }
}
