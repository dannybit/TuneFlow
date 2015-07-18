package com.dannybit.tuneflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by danielnamdar on 7/11/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tuneflow";

    private static final String TABLE_SONG = "songs";
    private static final String TABLE_PLAYLIST = "playlists";
    private static final String TABLE_PLAYLIST_SONG = "playlist_songs";

    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_PLAYLIST_NAME = "playlist_name";

    private static final String KEY_SONG_TRACK_ID =  "song_track_id";
    private static final String KEY_SONG_NAME = "song_name";
    private static final String KEY_SONG_DURATION = "song_duration";
    private static final String KEY_SONG_ARTWORK_LINK = "song_artwork_link";
    private static final String KEY_SONG_URL = "song_url";


    private static final String KEY_PLAYLIST_ID = "playlist_id";
    private static final String KEY_SONG_ID = "song_id";

    private static final String CREATE_TABLE_PLAYLIST = "create table" +  TABLE_PLAYLIST + "(" + KEY_ID
            + " integer primary key autoincrement, " + KEY_PLAYLIST_NAME
            + " text not null, " + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_SONG = "create table" + TABLE_SONG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_SONG_TRACK_ID + " TEXT, "
            + KEY_SONG_NAME + " TEXT, "
            + KEY_SONG_DURATION + "TEXT, "
            + KEY_SONG_ARTWORK_LINK + "TEXT, "
            + KEY_SONG_URL + "TEXT" + ")";

    private static String CREATE_TABLE_PLAYLIST_SONG = "create table" + TABLE_PLAYLIST_SONG + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_SONG_ID + "(" + "INTEGER, " + KEY_PLAYLIST_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME " + ")";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_SONG);
        db.execSQL(CREATE_TABLE_PLAYLIST_SONG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST_SONG);

        onCreate(db);
    }

    public long createPlaylist(Playlist playlist){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYLIST_NAME, playlist.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        long playlist_id = db.insert(TABLE_PLAYLIST, null, values);

        for (int i = 0; i < playlist.getSongs().size(); i++){
            createPlaylistSong(playlist_id, playlist.getSongs().get(i).getId());
        }

        return playlist_id;

    }


    public long createSong(Song song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SONG_NAME, song.getTrackName());
        values.put(KEY_SONG_DURATION, song.getDuration());
        values.put(KEY_SONG_URL, song.getUrl());
        values.put(KEY_SONG_ARTWORK_LINK, song.getArtworkLink());
        values.put(KEY_CREATED_AT, getDateTime());

        long song_id = db.insert(TABLE_SONG, null, values);
        return song_id;
    }

    public long createPlaylistSong(long playlist_id, long song_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYLIST_ID, playlist_id);
        values.put(KEY_SONG_ID, song_id);
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_PLAYLIST_SONG, null, values);
        return id;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }




}
