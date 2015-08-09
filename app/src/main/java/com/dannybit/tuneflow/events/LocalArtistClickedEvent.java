package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Artist;

/**
 * Created by danielnamdar on 8/8/15.
 */
public class LocalArtistClickedEvent {

    private Artist artist;

    public LocalArtistClickedEvent(Artist artist){
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }
}
