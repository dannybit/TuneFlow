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

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SongsListFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private SongAdapter adapter;
    private List<String> songLinks;
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
        songLinks = extras.getStringArrayList("SONG_LINKS");
        playListName = extras.getString("PLAYLIST_NAME");
        ((MainActivity) getActivity()).setActionBarTitle(playListName);
        setListAdapter(adapter);

        for (int i = 0; i < songLinks.size(); i++){
            RequestParams params = new RequestParams();
            params.put("client_id", SoundcloudRestClient.CLIENT_ID);
            SoundcloudRestClient.getTrack(songLinks.get(i), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Song song = new Song();
                    try {
                        song.setTrackName(response.getString("title"));
                        song.setArtworkLink(response.getString("artwork_url"));
                        song.setDuration(response.getString("duration"));
                        song.setUrl(addClientIdToUrl(response.getString("stream_url")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.add(song);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.v("HELLO", "array");
                }
            });
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(((Song)adapter.getItem(position)).getUrl());
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


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(String id);
    }

    public String addClientIdToUrl(String url){
        return url + "?client_id=" + SoundcloudRestClient.CLIENT_ID;
    }

    public SongAdapter getAdapter(){
        return this.adapter;
    }



}
