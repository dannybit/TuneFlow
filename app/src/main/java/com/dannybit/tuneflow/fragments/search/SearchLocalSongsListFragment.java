package com.dannybit.tuneflow.fragments.search;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.activities.SearchSongActivity;
import com.dannybit.tuneflow.database.LocalLibrary;
import com.dannybit.tuneflow.events.LocalSongClickedEvent;
import com.dannybit.tuneflow.fragments.search.adapters.SearchLocalSongsAdapter;
import com.dannybit.tuneflow.models.Album;
import com.dannybit.tuneflow.models.Artist;
import com.dannybit.tuneflow.models.LocalSong;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;


public class SearchLocalSongsListFragment extends ListFragment {


    private SearchLocalSongsAdapter adapter;
    private LocalLibrary localLibrary;
    private ArrayList<LocalSong> localSongs;

    public SearchLocalSongsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localLibrary = new LocalLibrary(getActivity().getContentResolver());
        Bundle extras = getArguments();
        if (extras != null && extras.getParcelable("ARTIST") != null) {
            localSongs = localLibrary.getArtistSongs(((Artist) extras.getParcelable("ARTIST")).getId());
        }
        else if (extras != null && extras.getParcelable("ALBUM") != null){
            localSongs = localLibrary.getAlbumSongs(((Album) extras.getParcelable("ALBUM")).getId());
        }

        else {
            localSongs = localLibrary.getSongs();
        }
        adapter = new SearchLocalSongsAdapter(getActivity(), localSongs);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        BusProvider.getInstance().post(new LocalSongClickedEvent((LocalSong) adapter.getItem(position)));
    }

    public void filter(String query){
        adapter.getFilter().filter(query);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Bundle extras = getArguments();
        if (extras != null && extras.getParcelable("ARTIST") != null) {
            getActivity().getMenuInflater().inflate(R.menu.main, menu);
            ((SearchSongActivity)getActivity()).getSupportActionBar().setTitle(

                    ((Artist) extras.getParcelable("ARTIST")).getArtistName()

            );
        }
        else if (extras != null && extras.getParcelable("ALBUM") != null){
            getActivity().getMenuInflater().inflate(R.menu.main, menu);
        }
    }
    */
}
