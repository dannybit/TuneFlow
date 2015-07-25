package com.dannybit.tuneflow.fragments;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dannybit.tuneflow.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.services.AudioPlaybackService;
import com.squareup.picasso.Picasso;


public class NowPlayingFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private Song currentSong;
    private ImageView songArtwork;
    private TextView songName;
    private ImageButton bBackward;
    private ImageButton bForward;
    private ImageButton bPlayOrPause;
    private SeekBar songProgressBar;
    private Handler mHandler = new Handler();
    private OnMediaPlayerButtonClickedListener callback;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        currentSong = (Song) extras.getParcelable("SONG");


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupActionBar();
        setupDrawer();
    }

    public interface OnMediaPlayerButtonClickedListener {
        public void onForwardClicked();
        public void onBackwardClicked();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnMediaPlayerButtonClickedListener) activity;
        } catch (ClassCastException e ){
            throw new ClassCastException(activity.toString()
                    + " must implement OnMediaPlayerButtonClickedListener");
        }
    }

    private void setupActionBar(){
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }

    private void setupDrawer(){
        ((MainActivity) getActivity()).disableDrawer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        songName = (TextView) view.findViewById(R.id.playerSongTitle);
        songName.setText(currentSong.getTrackName());
        songArtwork = (ImageView) view.findViewById(R.id.playerSongThumbnail);


        currentSong.loadImage(getActivity(), songArtwork);


        bBackward = (ImageButton) view.findViewById(R.id.bBackward);

        bBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getMusicService().playBackwardSong();
                nextSong(((MainActivity) getActivity()).getMusicService().getCurrentSong());
                callback.onBackwardClicked();
            }
        });


        bPlayOrPause = (ImageButton) view.findViewById(R.id.bPlayOrPause);

        bPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPlaybackService musicService = ((MainActivity) getActivity()).getMusicService();
                if (musicService.isPlaying()) {
                    musicService.pauseSong();
                    bPlayOrPause.setImageResource(R.drawable.btn_play);


                } else {
                    musicService.resumeSong();
                    bPlayOrPause.setImageResource(R.drawable.btn_pause);
                }

            }
        });

        bForward = (ImageButton) view.findViewById(R.id.bForward);

        bForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = ((MainActivity) getActivity()).getMusicService().playForwardSong();
                if (result) {
                    nextSong(((MainActivity) getActivity()).getMusicService().getCurrentSong());
                    callback.onForwardClicked();
                }


            }
        });

        songProgressBar = (SeekBar) view.findViewById(R.id.songProgressBar);
        songProgressBar.setOnSeekBarChangeListener(this);
        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);
        updateProgressBar();
        return view;
    }

    public void nextSong(Song nextSong){
        currentSong = nextSong;
        songName.setText(currentSong.getTrackName());
        currentSong.loadImage(getActivity(), songArtwork);
        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);


    }

    public void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            AudioPlaybackService musicService = ((MainActivity) getActivity()).getMusicService();
            long totalDuration = musicService.getTotalDuration();
            long currentDuration = musicService.getCurrentDuration();
            int progress = (int) getProgressPercentage(currentDuration, totalDuration);
            songProgressBar.setProgress(progress);

            mHandler.postDelayed(this, 100);
        }
    };

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(updateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        AudioPlaybackService musicService = ((MainActivity) getActivity()).getMusicService();
        mHandler.removeCallbacks(updateTimeTask);
        int totalDuration = (int) musicService.getTotalDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);
        musicService.seekTo(currentPosition);
        updateProgressBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(updateTimeTask);
    }
}
