package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Album;

/**
 * Created by danielnamdar on 8/8/15.
 */
public class LocalAlbumClickedEvent {

    private Album album;

    public LocalAlbumClickedEvent(Album album){
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }
}
