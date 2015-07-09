package com.dannybit.tuneflow.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnamdar on 7/4/15.
 */
public class Playlist {
    private List<String> songsLinks;
    private String name;
    private String artworkLink;

    public Playlist(String name){
        this.name = name;
        songsLinks = new ArrayList<String>();
    }

    public void add(String songLink){
        songsLinks.add(songLink);
    }

    public String getName() {
        return name;
    }

    public List<String> getSongsLinks() {
        return songsLinks;
    }

    public int getSize(){
        return songsLinks.size();
    }

    public String getArtworkLink(){
        return artworkLink;
    }

    public void setArtworkLink(String artworkLink){
        this.artworkLink = artworkLink;
    }

}
