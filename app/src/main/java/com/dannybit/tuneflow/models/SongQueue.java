package com.dannybit.tuneflow.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by danielnamdar on 9/5/15.
 */
public class SongQueue {

    private final ArrayList<Song> songs;
    private int currentSongPosition;

    public SongQueue(){
        this.songs = new ArrayList<>();
    }

    public SongQueue(ArrayList<Song> queue){
        this.songs = queue;
        currentSongPosition = 0;
    }

    public void addSong(Song songToAdd){
        songs.add(songToAdd);
    }

    public void addSongs(List<Song> songsToAdd){
        songsToAdd.addAll(songsToAdd);
    }

    public Song nextSong(){
        return songs.get(++currentSongPosition);
    }

    public Song previousSong(){
        return songs.get(--currentSongPosition);
    }

    public Song currentSong(){
        return songs.get(currentSongPosition);
    }

    public void removeSong(Song song){
        int indexToRemove = songs.indexOf(song);
        songs.remove(indexToRemove);
    }

    public void setSongPosition(int songPosition){
        this.currentSongPosition = currentSongPosition;
    }

    public boolean isEmpty(){
        return songs.isEmpty();
    }


}
