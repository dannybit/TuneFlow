package com.dannybit.tuneflow.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by danielnamdar on 9/5/15.
 */
public class SongQueue {

    private final LinkedList<Song> queue;

    public SongQueue(){
        this.queue = new LinkedList<>();
    }

    public SongQueue(LinkedList<Song> queue){
        this.queue = queue;
    }

    public void addSong(Song songToAdd){
        queue.add(songToAdd);
    }


    public Song nextSong(){
        return queue.remove();
    }

    public SongQueue shuffle(){
        LinkedList<Song> queueToShuffle = new LinkedList<>(queue);
        Collections.shuffle(queueToShuffle);
        return new SongQueue(queueToShuffle);
    }


}
