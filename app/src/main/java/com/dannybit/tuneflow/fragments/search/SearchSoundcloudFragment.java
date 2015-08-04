package com.dannybit.tuneflow.fragments.search;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.views.adapters.SearchSoundcloudAdapter;

import java.util.List;


public class SearchSoundcloudFragment extends ListFragment {

    private List<Song> songs;
    private SearchSoundcloudAdapter adapter;
    private OnFragmentInteractionListener mListener;



    public SearchSoundcloudFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new SearchSoundcloudAdapter(getActivity());
        setListAdapter(adapter);

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

    public SearchSoundcloudAdapter getAdapter(){
        return this.adapter;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(((Song)adapter.getItem(position)));
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Song song);
    }






}
