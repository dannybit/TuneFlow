package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Playlist;

/**
 * Created by danielnamdar on 9/6/15.
 */
public class AddPlaylistToQueueEvent {

    private Playlist playlist;

    public AddPlaylistToQueueEvent(Playlist playlist){
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
