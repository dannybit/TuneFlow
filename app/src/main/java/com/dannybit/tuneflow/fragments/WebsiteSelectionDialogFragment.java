package com.dannybit.tuneflow.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.WebsiteSelectedEvent;
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
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(R.string.website_selection_dialog_title);
        View view = inflater.inflate(R.layout.fragment_website_selection_dialog, container, false);
        Button localButton = (Button) view.findViewById(R.id.local_button);
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new WebsiteSelectedEvent(WebsiteSelection.LOCAL));
            }
        });
        Button soundcloudButton = (Button) view.findViewById(R.id.soundcloud_button);
        soundcloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new WebsiteSelectedEvent(WebsiteSelection.SOUNDCLOUD));
            }
        });


        return view;
    }




}
