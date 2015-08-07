package com.dannybit.tuneflow.fragments.search;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.dannybit.tuneflow.database.LocalLibrary;
import com.dannybit.tuneflow.fragments.search.adapters.SearchLocalSongsAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchLocalSongsListFragment extends ListFragment {


    private SearchLocalSongsAdapter adapter;
    private LocalLibrary localLibrary;

    public SearchLocalSongsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localLibrary = new LocalLibrary(getActivity().getContentResolver());
        adapter = new SearchLocalSongsAdapter(getActivity(), localLibrary.getSongs());
        setListAdapter(adapter);
    }






}
