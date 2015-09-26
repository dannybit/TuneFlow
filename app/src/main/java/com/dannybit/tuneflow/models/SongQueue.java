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

    private final List<Song> songs;
    private int currentSongPosition;
    private boolean repeat;
    private boolean repeatOnce;

    public SongQueue(){
        this.songs = new ArrayList<>();
    }

    public SongQueue(List<Song> queue){
        this.songs = queue;
        this.currentSongPosition = 0;
    }

    public SongQueue(List<Song> queue, int position){
        if (position >= queue.size() || position < 0){
            throw new IllegalArgumentException("position must be between 0 and (queue's size - 1)");
        }
        this.songs = queue;
        this.currentSongPosition = position;
    }

    public void addSong(Song songToAdd){
        songs.add(songToAdd);
    }

    public void addSongs(List<Song> songsToAdd){
        songsToAdd.addAll(songsToAdd);
    }

    public boolean moveForward(){
        if (currentSongPosition + 1 >= songs.size()){
            return false;
        }
        else {
            currentSongPosition++;
            return true;
        }
    }

    public boolean moveBackward() {
        if (currentSongPosition - 1 < 0) {
            return false;
        }
        else {
            currentSongPosition--;
            return true;
        }
    }

    public Song getCurrentSong(){
        return songs.get(currentSongPosition);
    }

    public void removeSong(Song song){
        int indexToRemove = songs.indexOf(song);
        songs.remove(indexToRemove);
    }

    public void setSongPosition(int songPosition){
        this.currentSongPosition = songPosition;
    }

    public boolean isEmpty(){
        return songs.isEmpty();
    }

    public void enableRepeat(){
        this.repeat = true;
    }

    public void enableRepeatOnce(){
        this.repeatOnce = true;
    }

    public void disableRepeat(){
        this.repeat = false;
    }

    public boolean isRepeat(){
        return this.repeat;
    }
}
