package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Playlist;

/**
 * Created by danielnamdar on 8/30/15.
 */
public class DeletePlaylistEvent {

    private Playlist playlist;

    public DeletePlaylistEvent(Playlist playlist){
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
