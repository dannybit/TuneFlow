package com.dannybit.tuneflow.database;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.dannybit.tuneflow.models.Album;
import com.dannybit.tuneflow.models.Artist;
import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnamdar on 8/6/15.
 *
 * // thanks:
 // https://github.com/CyanogenMod/android_packages_apps_Apollo/tree/cm-11.0/src/com/andrew/apollo
 */
public class LocalLibrary {
    private ContentResolver resolver;

    public LocalLibrary(ContentResolver resolver) {
        this.resolver = resolver;
    }



    private final Cursor makeSongCursor(){
        //Some audio may be explicitly marked as not being music
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,

        };

        return resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
                );
    }

    public ArrayList<LocalSong> getSongs(){
        ArrayList<LocalSong> songs = new ArrayList<>();
        Cursor cursor = makeSongCursor();
        if (cursor != null && cursor.moveToFirst()){
            do {
                final long id = cursor.getLong(0);
                final String artist = cursor.getString(1);
                final String title = cursor.getString(2);
                final String data = cursor.getString(3);
                final String duration = cursor.getString(5);
                final long albumId = cursor.getLong(6);

                LocalSong localSong = new LocalSong();
                localSong.setId(id);
                localSong.setArtist(artist);
                localSong.setTrackName(title);
                localSong.setDuration(duration);
                localSong.setUrl(data);


                localSong.setArtworkLink(getAlbumArtUri(albumId));
                songs.add(localSong);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return songs;
    }

    private final Cursor makeArtistSongCursor(long artistId){
        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.ARTIST_ID + "=" + artistId);
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,

        };

        return resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection.toString(),
                null,
                null
        );
    }

    public ArrayList<LocalSong> getArtistSongs(long artistId){
        ArrayList<LocalSong> songs = new ArrayList<>();
        Cursor cursor = makeArtistSongCursor(artistId);
        if (cursor != null && cursor.moveToFirst()){
            do {
                final long id = cursor.getLong(0);
                final String artist = cursor.getString(1);
                final String title = cursor.getString(2);
                final String data = cursor.getString(3);
                final String duration = cursor.getString(5);
                final long albumId = cursor.getLong(6);

                LocalSong localSong = new LocalSong();
                localSong.setId(id);
                localSong.setArtist(artist);
                localSong.setTrackName(title);
                localSong.setDuration(duration);
                localSong.setUrl(data);


                localSong.setArtworkLink(getAlbumArtUri(albumId));
                songs.add(localSong);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return songs;
    }

    private String getAlbumArtUri(long albumId){
        Cursor imageCursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.AlbumColumns.ALBUM_ART},
                MediaStore.Audio.Media._ID+" =?",
                new String[]{String.valueOf(albumId)},
                null);
        imageCursor.moveToFirst();
        return imageCursor.getString(0);
    }


    private final Cursor makeArtistCursor() {
        return resolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.ArtistColumns.ARTIST,
                        MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS,
                        MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
                }, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }

    public ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<Artist>();
        Cursor cursor = makeArtistCursor();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final long id = cursor.getLong(0);
                final String artistName = cursor.getString(1);
                final int albumCount = cursor.getInt(2);
                final int songCount = cursor.getInt(3);
                final Artist artist = new Artist(id, artistName, songCount, albumCount);
                artists.add(artist);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return artists;
    }

    private final Cursor makeAlbumCursor() {
        return resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {
            BaseColumns._ID,
                        MediaStore.Audio.AlbumColumns.ALBUM,
                        MediaStore.Audio.AlbumColumns.ARTIST,
                        MediaStore.Audio.AlbumColumns.ALBUM_ART,
                        MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
                }, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    private final Cursor makeArtistAlbumCursor(final Long artistId) {
        return resolver.query(MediaStore.Audio.Artists.Albums.getContentUri("external", artistId),
                new String[] {
                        BaseColumns._ID,
                        MediaStore.Audio.AlbumColumns.ALBUM,
                        MediaStore.Audio.AlbumColumns.ARTIST,
                        MediaStore.Audio.AlbumColumns.ALBUM_ART,
                        MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,

                }, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public ArrayList<Album> getAlbums(final Long artistId) {
        ArrayList<Album> albums = new ArrayList<Album>();
        Cursor cursor;
        if (artistId != null) {
            cursor = makeArtistAlbumCursor(artistId);
        } else {
            cursor = makeAlbumCursor();
        }
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final long id = cursor.getLong(0);
                final String albumName = cursor.getString(1);
                final String artist = cursor.getString(2);
                final String albumArt = cursor.getString(3);
                final int songCount = cursor.getInt(4);
                final Album album = new Album(id, albumName,artist, albumArt, songCount);
                albums.add(album);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return albums;
    }


    private final Cursor makeAlbumSongCursor(final Long albumId) {
        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.ALBUM_ID + "=" + albumId);
        return resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
          /* 0 */ MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION
                }, selection.toString(), null, MediaStore.Audio.Media.TRACK + ", " + MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public ArrayList<LocalSong> getAlbumSongs(final Long albumId) {
        ArrayList<LocalSong> localSongs = new ArrayList<LocalSong>();
        Cursor cursor = makeAlbumSongCursor(albumId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final long id = cursor.getLong(0);
                final String artist = cursor.getString(1);
                final String songName = cursor.getString(2);
                final String songUrl = cursor.getString(3);
                final String duration = cursor.getString(4);

                LocalSong localSong = new LocalSong();
                localSong.setId(id);
                localSong.setArtist(artist);
                localSong.setTrackName(songName);
                localSong.setDuration(duration);
                localSong.setUrl(songUrl);

                localSongs.add(localSong);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return localSongs;
    }



}
