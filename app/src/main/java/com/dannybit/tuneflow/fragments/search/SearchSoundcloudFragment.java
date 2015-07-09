package com.dannybit.tuneflow.fragments.search;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.views.adapters.SearchSoundcloudAdapter;

import java.util.List;


public class SearchSoundcloudFragment extends ListFragment {

    private List<Song> songs;
    private SearchSoundcloudAdapter adapter;



    public SearchSoundcloudFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchSoundcloudAdapter(getActivity());
        setListAdapter(adapter);

    }

    public SearchSoundcloudAdapter getAdapter(){
        return this.adapter;
    }







}
