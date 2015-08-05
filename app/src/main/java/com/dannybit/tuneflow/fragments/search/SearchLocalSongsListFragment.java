package com.dannybit.tuneflow.fragments.search;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.fragments.search.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;


public class SearchLocalSongsListFragment extends ListFragment {


    public SearchLocalSongsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, getAllLocalArtists()));
    }

    private List<String> getAllLocalSongs(){
        //Some audio may be explicitly marked as not being music
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = getActivity().managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        List<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            songs.add(cursor.getString(2) );
        }

        return songs;

    }

    private List<String> getAllLocalArtists(){
        //Some audio may be explicitly marked as not being music


        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
        };

        Cursor cursor = getActivity().managedQuery(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        List<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            songs.add(cursor.getString(1));
        }

        return songs;
    }



}
