package com.dannybit.tuneflow.fragments;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.activities.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.RepeatButtonClickedEvent;
import com.dannybit.tuneflow.events.SongSelectedEvent;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.services.AudioPlaybackService;


public class NowPlayingFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private Song currentSong;
    private ImageView songArtwork;
    private TextView songName;
    private ImageButton bBackward;
    private ImageButton bForward;
    private ImageButton bPlayOrPause;
    private ImageButton bRepeat;
    private SeekBar songProgressBar;
    private Handler mHandler = new Handler();
    private OnMediaPlayerButtonClickedListener callback;
    private ImageView slidingPlayerSongThumbnail;
    private ImageButton bSlidingPlayerPlayOrPause;
    private TextView songCurrentDurationText;
    private TextView songTotalDurationText;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        currentSong = extras.getParcelable("SONG");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnMediaPlayerButtonClickedListener {
        void onForwardClicked();
        void onBackwardClicked();
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);


        slidingPlayerSongThumbnail = (ImageView) view.findViewById(R.id.slidingPlayerSongThumbnail);
        bSlidingPlayerPlayOrPause = (ImageButton) view.findViewById(R.id.bSlidingPlayerPlayOrPause);
        songName = (TextView) view.findViewById(R.id.playerSongTitle);
        songArtwork = (ImageView) view.findViewById(R.id.playerSongThumbnail);
        bBackward = (ImageButton) view.findViewById(R.id.bBackward);
        bPlayOrPause = (ImageButton) view.findViewById(R.id.bPlayOrPause);
        bForward = (ImageButton) view.findViewById(R.id.bForward);
        bRepeat = (ImageButton) view.findViewById(R.id.bRepeat);
        songProgressBar = (SeekBar) view.findViewById(R.id.songProgressBar);
        songCurrentDurationText = (TextView) view.findViewById(R.id.songCurrentDurationText);
        songTotalDurationText = (TextView) view.findViewById(R.id.songTotalDurationText);


        songName.setText(currentSong.getTrackName());
        songTotalDurationText.setText(currentSong.getDurationInMins());
        songCurrentDurationText.setText("00:00");
        currentSong.loadImage(getActivity(), songArtwork);
        currentSong.loadThumbnail(getActivity(), slidingPlayerSongThumbnail);
        if (!getArguments().getBoolean("IS_PLAYING")) {
            songProgressBar.setEnabled(true);
        }

        songProgressBar.setOnSeekBarChangeListener(this);


        bBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getMusicService().playBackwardSong();
                updateSong(((MainActivity) getActivity()).getMusicService().getCurrentSong(), false);
                callback.onBackwardClicked();
            }
        });

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

        bSlidingPlayerPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPlaybackService musicService = ((MainActivity) getActivity()).getMusicService();
                if (musicService.isPlaying()) {
                    musicService.pauseSong();
                    bSlidingPlayerPlayOrPause.setImageResource(R.drawable.btn_play);


                } else {
                    musicService.resumeSong();
                    bSlidingPlayerPlayOrPause.setImageResource(R.drawable.btn_pause);
                }
            }
        });

        bForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = ((MainActivity) getActivity()).getMusicService().playForwardSong();
                if (result) {
                    updateSong(((MainActivity) getActivity()).getMusicService().getCurrentSong(), false);
                    callback.onForwardClicked();
                }
            }
        });

        bRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new RepeatButtonClickedEvent());
            }
        });


        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);
        updateProgressBar();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProgressBar();
    }

    public void hideSlidingPlayOrPauseButton(){
        bSlidingPlayerPlayOrPause.setVisibility(View.GONE);
    }

    public void showSlidingPlayOrPauseButton(){
        bSlidingPlayerPlayOrPause.setVisibility(View.VISIBLE);
    }

    public void enableRepeatButton(){
        bRepeat.setImageResource(R.drawable.btn_repeat_clicked);
    }

    public void disableRepeatButton(){
        bRepeat.setImageResource(R.drawable.btn_repeat);
    }

    public void updateSong(Song newSong, boolean isPlaying){
        currentSong = newSong;
        songName.setText(currentSong.getTrackName());
        songCurrentDurationText.setText("00:00");
        songTotalDurationText.setText(currentSong.getDurationInMins());
        currentSong.loadImage(getActivity(), songArtwork);
        currentSong.loadThumbnail(getActivity(), slidingPlayerSongThumbnail);
        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);
        if (!isPlaying) {
            disableProgressBar();
        }
        updateProgressBar();
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
            songCurrentDurationText.setText(musicService.getCurrentDurationInMins());

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

    public void enableProgressBar(){
        if (songProgressBar != null){
            songProgressBar.setEnabled(true);
        }
    }

    public void disableProgressBar(){
        if (songProgressBar != null){
            songProgressBar.setEnabled(false);
        }
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
