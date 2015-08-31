package com.dannybit.tuneflow.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.activities.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.database.DatabaseHelper;
import com.dannybit.tuneflow.events.DeletePlaylistEvent;
import com.dannybit.tuneflow.events.PlaylistSelectedEvent;
import com.dannybit.tuneflow.events.RenamePlaylistEvent;
import com.dannybit.tuneflow.events.SongSelectedEvent;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.fragments.adapters.PlaylistAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;


public class PlaylistListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {


    private PlaylistAdapter adapter;
    private DatabaseHelper dbHelper;
    private static final String PLAYLIST_FRAGMENT_TITLE = "Playlists";
    private GridView playlistsGridView;
    private FloatingActionButton fabPlaylist;

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
        playlistsGridView.setOnItemLongClickListener(this);
        return view;
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
        Playlist selectedPlaylist = (Playlist) adapter.getItem(position);
        BusProvider.getInstance().post(new PlaylistSelectedEvent(selectedPlaylist));
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        final Playlist selectedPlaylist = (Playlist) adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] arr = {"Play", "Rename", "Delete", "Add Tracks to Playlist"};
        builder.setTitle(selectedPlaylist.getName()).setItems(arr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Play
                        if (selectedPlaylist.getSize() > 0) {
                            BusProvider.getInstance().post(new SongSelectedEvent(selectedPlaylist.getSongs(), 0));
                        } else {
                            Toast toast = Toast.makeText(getActivity(), R.string.playlist_is_empty_message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case 1: // Rename
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        EditPlaylistDialogFragment editPlaylistDialogFragment = new EditPlaylistDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("PLAYLIST", selectedPlaylist);
                        editPlaylistDialogFragment.setArguments(bundle);
                        editPlaylistDialogFragment.show(fm, "show");
                        break;
                    case 2: // Delete
                        BusProvider.getInstance().post(new DeletePlaylistEvent(selectedPlaylist));
                        break;
                    case 3: // Add Tracks to Playlist
                        //BusProvider.getInstance().post(new PlaylistSelectedEvent(selectedPlaylist));
                        break;

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
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

    public void scrollToBottom(){
        playlistsGridView.smoothScrollToPosition(adapter.getCount() - 1);
    }

    public PlaylistAdapter getAdapter() {
        return adapter;
    }
}
