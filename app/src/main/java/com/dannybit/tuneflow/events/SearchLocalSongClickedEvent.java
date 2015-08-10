package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.LocalSong;

/**
 * Created by danielnamdar on 8/8/15.
 */
public class SearchLocalSongClickedEvent {

    private LocalSong song;

    public SearchLocalSongClickedEvent(LocalSong song){
        this.song = song;
    }

    public LocalSong getSong(){
        return this.song;
    }


}
