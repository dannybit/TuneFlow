package com.dannybit.tuneflow.fragments.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.database.LocalLibrary;
import com.dannybit.tuneflow.fragments.search.adapters.SearchLocalAlbumsAdapter;

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


}
