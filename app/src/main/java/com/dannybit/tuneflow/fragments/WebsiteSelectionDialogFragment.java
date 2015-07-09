package com.dannybit.tuneflow.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.SearchSongActivity;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebsiteSelectionDialogFragment extends DialogFragment {


    public WebsiteSelectionDialogFragment() {
        // Required empty public constructor
    }



    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_website_selection_dialog, container, false);
        Button soundcloudButton = (Button) view.findViewById(R.id.soundcloud_button);
        soundcloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchSongActivity.class);
                intent.putExtra("SELECTION", WebsiteSelection.SOUNDCLOUD);
                startActivityForResult(intent, SongsListFragment.FIND_SOUNDClOUD_SONG_REQUEST);
            }
        });
        return view;
    }




}
