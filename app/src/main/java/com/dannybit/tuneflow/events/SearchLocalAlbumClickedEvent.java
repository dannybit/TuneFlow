package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Album;

/**
 * Created by danielnamdar on 8/8/15.
 */
public class SearchLocalAlbumClickedEvent {

    private Album album;

    public SearchLocalAlbumClickedEvent(Album album){
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }
}
