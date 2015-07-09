package com.dannybit.tuneflow.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

public class AudioPlaybackService extends Service {

    private Context context;
    private MediaPlayer mediaPlayer;
    private int songPosition;
    private ArrayList<Song> songs;

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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private void initMediaPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setList(ArrayList<Song> songs){
        this.songs = songs;
    }


}
