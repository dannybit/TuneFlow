package com.dannybit.tuneflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.models.SongType;
import com.dannybit.tuneflow.models.SoundcloudSong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by danielnamdar on 7/11/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper dbInstance;

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "tuneflow.db";

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
    private static final String KEY_SONG_TYPE = "song_type";


    private static final String KEY_PLAYLIST_ID = "playlist_id";
    private static final String KEY_SONG_ID = "song_id";

    private static final String CREATE_TABLE_PLAYLIST = "create table " +  TABLE_PLAYLIST + "(" + KEY_ID
            + " integer primary key autoincrement, " + KEY_PLAYLIST_NAME
            + " text not null, " + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_SONG = "create table " + TABLE_SONG + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_SONG_TRACK_ID + " TEXT, "
            + KEY_SONG_NAME + " TEXT, "
            + KEY_SONG_DURATION + " TEXT, "
            + KEY_SONG_ARTWORK_LINK + " TEXT, "
            + KEY_SONG_URL + " TEXT, "
            + KEY_SONG_TYPE + " TEXT, "
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static String CREATE_TABLE_PLAYLIST_SONG = "create table " + TABLE_PLAYLIST_SONG + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_SONG_ID + " INTEGER, " + KEY_PLAYLIST_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME " + ")";


    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbInstance == null) {
            dbInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return dbInstance;
    }


    private DatabaseHelper(Context context){
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

    public int renamePlaylist(Playlist playlist, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_PLAYLIST_NAME, newName);
        int rowsAffected = db.update(TABLE_PLAYLIST, newValues, "id=?", new String[]{String.valueOf(playlist.getId())});
        return rowsAffected;
    }

    public int deletePlaylist(Playlist playlist){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PLAYLIST, "id=?", new String[]{String.valueOf(playlist.getId())});
        return rowsAffected;
    }

    public int deleteSong(Song song, Playlist playlist){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, "id=?", new String[]{String.valueOf(song.getId())});
        int rowsAffected = db.delete(TABLE_PLAYLIST_SONG, "song_id=? AND playlist_id=?", new String[]{String.valueOf(song.getId()), String.valueOf(playlist.getId())});
        return rowsAffected;
    }


    public long createSong(Song song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SONG_NAME, song.getTrackName());
        values.put(KEY_SONG_DURATION, song.getDuration());
        values.put(KEY_SONG_URL, song.getUrl());
        values.put(KEY_SONG_ARTWORK_LINK, song.getArtworkLink());
        values.put(KEY_SONG_TYPE, song.getSongType().name());
        values.put(KEY_CREATED_AT, getDateTime());

        long song_id = db.insert(TABLE_SONG, null, values);
        song.setId(song_id);
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

    public Song getSong(long song_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SONG + " WHERE " +
                KEY_ID + " = " + song_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }

        return createSongFromCursor(c);
    }

    private Song createSongFromCursor(Cursor c){
        Song song = null;
        switch (SongType.valueOf(c.getString(c.getColumnIndex(KEY_SONG_TYPE)))){
            case LOCAL:
                song = new LocalSong();
                break;
            case SOUNDCLOUD:
                song = new SoundcloudSong();
                break;
        }

        if (song != null) {
            song.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            song.setTrackName(c.getString(c.getColumnIndex(KEY_SONG_NAME)));
            song.setUrl(c.getString(c.getColumnIndex(KEY_SONG_URL)));
            song.setDuration(c.getString(c.getColumnIndex(KEY_SONG_DURATION)));
            song.setArtworkLink(c.getString(c.getColumnIndex(KEY_SONG_ARTWORK_LINK)));
            // song.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }
        return song;
    }

    public List<Song> getAllSongs(){
        List<Song> songs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SONG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Song song = createSongFromCursor(c);
                if (song != null) {
                    songs.add(song);
                }
            } while (c.moveToNext());
        }
        return songs;
    }

    public Playlist getPlaylist(long playlist_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYLIST + " WHERE " + KEY_ID + " = " + playlist_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null){
            c.moveToFirst();
        }
        Playlist playlist = new Playlist();
        playlist.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        playlist.setName(c.getString(c.getColumnIndex(KEY_SONG_NAME)));
        return playlist;
    }

    public List<Playlist> getAllPlaylist(){
        List<Playlist> playlists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYLIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Playlist playlist = new Playlist();
                playlist.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                playlist.setName(c.getString(c.getColumnIndex(KEY_PLAYLIST_NAME)));
                playlist.setSongs((ArrayList) getAllSongsInPlaylist(playlist.getId()));
                playlists.add(playlist);
            } while (c.moveToNext());
        }


        return playlists;
    }


    public List<Song> getAllSongsInPlaylist(long playlist_id){
        List<Song> songs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " +  TABLE_SONG + " ts, "
                + TABLE_PLAYLIST + " tp, " + TABLE_PLAYLIST_SONG + " tsp WHERE tp."
                + KEY_ID + " = " + playlist_id + " AND tp." + KEY_ID + " = "
                + "tsp." + KEY_PLAYLIST_ID + " AND ts."  + KEY_ID + " = " + "tsp." + KEY_SONG_ID;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Song song = createSongFromCursor(c);
                if (song != null) {
                    songs.add(song);
                }
            } while (c.moveToNext());
        }
        return songs;
    }





}
