package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.LocalSong;

/**
 * Created by danielnamdar on 8/8/15.
 */
public class LocalSongClickedEvent {

    private LocalSong song;

    public LocalSongClickedEvent(LocalSong song){
        this.song = song;
    }

    public LocalSong getSong(){
        return this.song;
    }


}
