package com.dannybit.tuneflow.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;
import com.squareup.picasso.Picasso;


public class NowPlayingFragment extends Fragment {

    private Song currentSong;
    private ImageView songArtwork;
    private TextView songName;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        currentSong = (Song) extras.getParcelable("SONG");
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        songName = (TextView) view.findViewById(R.id.playerSongTitle);
        songName.setText(currentSong.getTrackName());
        songArtwork = (ImageView) view.findViewById(R.id.playerSongThumbnail);
        Picasso.with(getActivity()).load(currentSong.getArtwork500x500()).error(R.drawable.soundcloud_icon).into(songArtwork);
      return view;
    }




}
