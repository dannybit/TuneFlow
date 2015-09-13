package com.dannybit.tuneflow.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.activities.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.DeleteSongEvent;
import com.dannybit.tuneflow.events.SongSelectedEvent;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.fragments.adapters.SongAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class SongsListFragment extends ListFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private SongAdapter adapter;
    private ArrayList<Song> songs;
    private Playlist playlist;
    private WebsiteSelectionDialogFragment webSelectionFragment;
    private FloatingActionButton fabSongsList;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongsListFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webSelectionFragment = new WebsiteSelectionDialogFragment();
        // Needed to intercept menu click
        setHasOptionsMenu(true);
        Bundle extras = getArguments();
        songs = extras.getParcelableArrayList("SONGS");
        playlist = (Playlist) extras.getParcelable("PLAYLIST");
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
        ((MainActivity) getActivity()).setActionBarTitle(playlist.getName());
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
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Song songSelected = (Song) adapter.getItem(position);
        final int songPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] arr = {"Play",  "Delete"};
        builder.setTitle(songSelected.getTrackName()).setItems(arr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Play
                        BusProvider.getInstance().post(new SongSelectedEvent(songs, songPosition));
                        break;
                    case 1: // Delete but doesnt update the queue
                        BusProvider.getInstance().post(new DeleteSongEvent(songSelected, playlist));
                        break;

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
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
