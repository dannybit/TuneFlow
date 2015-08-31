package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Playlist;

/**
 * Created by danielnamdar on 8/30/15.
 */
public class RenamePlaylistEvent {

    private Playlist playlist;
    private String newName;

    public RenamePlaylistEvent(Playlist playlist, String newName){
        this.playlist = playlist;
        this.newName = newName;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public String getNewName() {
        return newName;
    }
}
