package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;

/**
 * Created by danielnamdar on 9/6/15.
 */
public class DeleteSongEvent {

    private Song song;
    private Playlist playlist;

    public DeleteSongEvent(Song song, Playlist playlist){
        this.song =  song;
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Song getSong() {
        return song;
    }
}
