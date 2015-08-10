package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Playlist;

/**
 * Created by danielnamdar on 8/10/15.
 */
public class PlaylistSelectedEvent {

    private Playlist playlist;

    public  PlaylistSelectedEvent(Playlist playlist){
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
