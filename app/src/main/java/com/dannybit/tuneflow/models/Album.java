package com.dannybit.tuneflow.models;

import android.content.Context;
import android.widget.ImageView;

import com.dannybit.tuneflow.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class Album {


    private long id;
    private String name;
    private String albumArtUri;
    private String artist;
    private int songCount;

    public String getAlbumArtUri() {
        return albumArtUri;
    }

    public void setAlbumArtUri(String albumArtUri) {
        this.albumArtUri = albumArtUri;
    }

    public Album(long id, String name, String artist, String albumArtUri,int songCount) {
        this.id = id;
        this.name = name;
        this.albumArtUri = albumArtUri;

        this.artist = artist;
        this.songCount = songCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public void loadImage(Context context, ImageView imageView){
        if (getAlbumArtUri() != null) {
            Picasso.with(context).load(new File(getAlbumArtUri())).error(R.drawable.web_hi_res_512).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.web_hi_res_512).into(imageView);
        }
    }

}
