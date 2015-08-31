package com.dannybit.tuneflow.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnamdar on 7/4/15.
 **/

public class Playlist implements Parcelable {
    private long id;
    private ArrayList<Song> songs;
    private String name;

    public Playlist(){

    }

    public Playlist(String name){
        this.name = name;
        songs = new ArrayList<Song>();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name){
        this.name = name;
    }


    public void add(Song song){
        songs.add(song);
    }

    public String getName() {
        return name;
    }

    public void setSongs(ArrayList<Song> songs){ this.songs = songs;}

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getSize(){
        return songs.size();
    }


    protected Playlist(Parcel in) {
        id = in.readLong();
        if (in.readByte() == 0x01) {
            songs = new ArrayList<Song>();
            in.readList(songs, Song.class.getClassLoader());
        } else {
            songs = null;
        }
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        if (songs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(songs);
        }
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
