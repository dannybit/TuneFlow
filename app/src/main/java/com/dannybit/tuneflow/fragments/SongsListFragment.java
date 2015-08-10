package com.dannybit.tuneflow.fragments;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.activities.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.SongSelectedEvent;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.views.adapters.SongAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class SongsListFragment extends ListFragment implements View.OnClickListener {

    private SongAdapter adapter;
    private ArrayList<Song> songs;
    private String playListName;
    private WebsiteSelectionDialogFragment webSelectionFragment;
    private FloatingActionButton fabSongsList;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webSelectionFragment = new WebsiteSelectionDialogFragment();
        // Needed to intercept menu click
        setHasOptionsMenu(true);
        Bundle extras = getArguments();
        songs = extras.getParcelableArrayList("SONGS");
        playListName = extras.getString("PLAYLIST_NAME");
        setupAdapter();
        setupActionBarTitle();
    }

    private void setupAdapter(){
        adapter = new SongAdapter(getActivity());
        setListAdapter(adapter);

        for (int i = 0; i < songs.size(); i++){
            adapter.add(songs.get(i));
        }
    }

    private void setupActionBarTitle(){
        ((MainActivity) getActivity()).setActionBarTitle(playListName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs_list, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        fabSongsList = (FloatingActionButton) view.findViewById(R.id.fabSongsList);
        fabSongsList.attachToListView(listView);
        fabSongsList.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        addNewSong();
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        BusProvider.getInstance().post(new SongSelectedEvent(songs, position));

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                return false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setupActionBar();
    }

    private void setupActionBar(){
        ((MainActivity) getActivity()).getSupportActionBar().show();
    }

    private void addNewSong(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        webSelectionFragment.show(fm, "show");
    }

    public void closeSelectionDialog(){
        webSelectionFragment.dismiss();
    }



    public SongAdapter getAdapter(){
        return this.adapter;
    }

    public void addSongToList(Song song){
        getAdapter().add(song);
        getAdapter().notifyDataSetChanged();
        songs.add(song);
    }

}
