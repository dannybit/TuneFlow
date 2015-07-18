package com.dannybit.tuneflow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.views.adapters.PlaylistAdapter;



public class PlaylistListFragment extends ListFragment {


    private PlaylistAdapter adapter;
    private OnPlaylistSelectedListener callback;

    public interface OnPlaylistSelectedListener {
        public void onPlaylistSelected(Playlist playlist);
    }


    public PlaylistListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Needed to intercept menu click
        setHasOptionsMenu(true);
        adapter = new PlaylistAdapter(getActivity());
        Playlist playlist = new Playlist("TEST");
        adapter.add(playlist);
        setListAdapter(adapter);
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

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        callback.onPlaylistSelected((Playlist) adapter.getItem(position));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Log.v("HELLO", "PLAYLIST");
                return true;
            default:
                return false;
        }

    }
}
