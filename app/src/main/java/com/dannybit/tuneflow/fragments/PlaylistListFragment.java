package com.dannybit.tuneflow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.dannybit.tuneflow.activities.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.database.DatabaseHelper;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.views.adapters.PlaylistAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;


public class PlaylistListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private PlaylistAdapter adapter;
    private OnPlaylistSelectedListener callback;
    private DatabaseHelper dbHelper;
    private static final String PLAYLIST_FRAGMENT_TITLE = "Playlists";
    private GridView playlistsGridView;
    private FloatingActionButton fabPlaylist;


    public interface OnPlaylistSelectedListener {
        void onPlaylistSelected(Playlist playlist);
    }

    public PlaylistListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dbHelper = DatabaseHelper.getInstance(getActivity());
        setupAdapter();
        setupActionBarTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
        playlistsGridView = (GridView) view.findViewById(R.id.playlistsGridview);
        fabPlaylist = (FloatingActionButton) view.findViewById(R.id.fabPlaylist);
        fabPlaylist.attachToListView(playlistsGridView);
        fabPlaylist.setOnClickListener(this);
        playlistsGridView.setAdapter(adapter);
        playlistsGridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback =  (OnPlaylistSelectedListener) activity;
        } catch (ClassCastException e){
         throw new ClassCastException(activity.toString() + " must implement OnPlaylistSelectedListener");
        }
    }

    private void setupAdapter(){
        adapter = new PlaylistAdapter(getActivity());
        List<Playlist> playlists = dbHelper.getAllPlaylist();
        for (int i = 0; i < playlists.size(); i++){
            adapter.add(playlists.get(i));
        }
    }

    private void setupActionBarTitle(){
        ((MainActivity) getActivity()).setActionBarTitle(PLAYLIST_FRAGMENT_TITLE);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(PLAYLIST_FRAGMENT_TITLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        callback.onPlaylistSelected((Playlist) adapter.getItem(position));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                return false;
        }

    }

    @Override
    public void onClick(View view) {
        addNewPlaylist();
    }

    private void addNewPlaylist(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NewPlaylistDialogFragment newPlaylistDialogFragment = new NewPlaylistDialogFragment();
        newPlaylistDialogFragment.show(fm, "show");
    }

    public void addPlaylist(Playlist playlist){
        adapter.add(playlist);
        adapter.notifyDataSetChanged();
    }
}
