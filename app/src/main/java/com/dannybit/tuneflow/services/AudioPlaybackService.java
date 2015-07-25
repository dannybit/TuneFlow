package com.dannybit.tuneflow.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmManagerClient;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

public class AudioPlaybackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private Context context;
    private MediaPlayer mediaPlayer;
    private int songPosition;
    private ArrayList<Song> songs;
    private final IBinder musicBind = new MusicBinder();
    private SongCompletedListener songCompletedListener;
    private SongPreparedListener songPreparedListener;


    public AudioPlaybackService() {
        super();
    }

    public AudioPlaybackService(Context context)  {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }



    public void setSongPosition(int songPosition){
        this.songPosition = songPosition;
    }

    public Song getCurrentSong(){
        return songs.get(songPosition);
    }

    public boolean playSong(){
        // Need when selecting a new song
        mediaPlayer.setOnCompletionListener(null);
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(getCurrentSong().getUrl());
        } catch (Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
            return false;
        }

        mediaPlayer.prepareAsync();
        return true;
    }

    private void initMediaPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songs){
        this.songs = songs;
    }



    public class MusicBinder extends Binder {
        public AudioPlaybackService getService() {
            return AudioPlaybackService.this;
        }

        public void setSongCompletedListener(SongCompletedListener listener){
            songCompletedListener = listener;
        }

        public void setSongPreparedListener(SongPreparedListener listener){
            songPreparedListener = listener;
        }
    }

    public interface SongCompletedListener {
        public void songCompleted(Song nextSongToPlay);
    }

    public interface SongPreparedListener {
        public void songPrepared(Song song);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
        if (songPreparedListener != null){
            songPreparedListener.songPrepared(getCurrentSong());
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        songPosition++;
        if (songPosition > songs.size() - 1){
            songPosition--;
        } else {
            mediaPlayer.setOnCompletionListener(null);
            playSong();
            if (songCompletedListener != null){
                songCompletedListener.songCompleted(getCurrentSong());
            }
        }
    }

    public void playBackwardSong(){
        songPosition--;
        if (songPosition < 0){
            songPosition++;
        }
        mediaPlayer.setOnCompletionListener(null);
        playSong();
    }

    public boolean playForwardSong(){
        songPosition++;
        if (songPosition > songs.size() - 1){
            return false;
        } else {
            mediaPlayer.setOnCompletionListener(null);
            playSong();
        }
        return true;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    public void pauseSong(){
        mediaPlayer.pause();
    }

    public void resumeSong(){
        mediaPlayer.start();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public long getTotalDuration(){
        return mediaPlayer.getDuration();
    }

    public long getCurrentDuration(){
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }




}
