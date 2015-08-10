package com.dannybit.tuneflow.fragments.search;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.database.LocalLibrary;
import com.dannybit.tuneflow.events.SearchLocalAlbumClickedEvent;
import com.dannybit.tuneflow.fragments.search.adapters.SearchLocalAlbumsAdapter;
import com.dannybit.tuneflow.models.Album;

public class SearchLocalAlbumsListFragment extends ListFragment {


    private LocalLibrary localLibrary;
    private SearchLocalAlbumsAdapter adapter;

    public SearchLocalAlbumsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localLibrary = new LocalLibrary(getActivity().getContentResolver());
        adapter = new SearchLocalAlbumsAdapter(getActivity(), localLibrary.getAlbums(null));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        BusProvider.getInstance().post(new SearchLocalAlbumClickedEvent((Album) adapter.getItem(position)));
    }

    public void filter(String query){
        adapter.getFilter().filter(query);
    }
}
