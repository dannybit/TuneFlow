package com.dannybit.tuneflow.fragments;

import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


import com.dannybit.tuneflow.MainActivity;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.network.SoundcloudRestClient;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.views.adapters.SongAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SongsListFragment extends ListFragment {

    private OnSongSelectedListener callback;
    private SongAdapter adapter;
    private ArrayList<Song> songs;
    private String playListName;
    private WebsiteSelectionDialogFragment webSelectionFragment;



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
        adapter = new SongAdapter(getActivity());
        Bundle extras = getArguments();
        songs = extras.getParcelableArrayList("SONGS");
        playListName = extras.getString("PLAYLIST_NAME");
        ((MainActivity) getActivity()).setActionBarTitle(playListName);
        setListAdapter(adapter);

        for (int i = 0; i < songs.size(); i++){
            adapter.add(songs.get(i));
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnSongSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (callback != null) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            callback.onSongSelected(((Song)adapter.getItem(position)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                addNewSong();
                return true;
            default:
                return false;
        }

    }

    private void addNewSong(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        webSelectionFragment.show(fm, "show");
    }

    public void closeSelectionDialog(){
        webSelectionFragment.dismiss();
    }


    public interface OnSongSelectedListener {

        public void onSongSelected(Song song);
    }

    public SongAdapter getAdapter(){
        return this.adapter;
    }



}
