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
    private Song currentSong;

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
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    public void playSong(){
        try {
            mediaPlayer.setDataSource(currentSong.getUrl());
        } catch (Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        Log.v("HELLO", "set data");
        mediaPlayer.prepareAsync();
    }

    private void initMediaPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songs){
        this.songs = songs;
    }

    public void setSong(Song song){
        this.currentSong = song;
    }

    public class MusicBinder extends Binder {
        public AudioPlaybackService getService() {
            return AudioPlaybackService.this;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }



}
