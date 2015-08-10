package com.dannybit.tuneflow.fragments.search;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.database.LocalLibrary;
import com.dannybit.tuneflow.events.LocalArtistClickedEvent;
import com.dannybit.tuneflow.fragments.search.adapters.SearchLocalArtistsAdapter;
import com.dannybit.tuneflow.models.Artist;


public class SearchLocalArtistsListFragment extends ListFragment {


    private SearchLocalArtistsAdapter adapter;
    private LocalLibrary localLibrary;

    public SearchLocalArtistsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localLibrary = new LocalLibrary(getActivity().getContentResolver());
        adapter = new SearchLocalArtistsAdapter(getActivity(), localLibrary.getArtists());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        BusProvider.getInstance().post(new LocalArtistClickedEvent((Artist) adapter.getItem(position)));
    }

    public void filter(String query){
        adapter.getFilter().filter(query);
    }


}
