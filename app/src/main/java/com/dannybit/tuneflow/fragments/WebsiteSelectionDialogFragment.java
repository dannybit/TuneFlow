package com.dannybit.tuneflow.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.WebsiteSelectedEvent;
import com.dannybit.tuneflow.fragments.adapters.WebSelectionAdapter;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebsiteSelectionDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

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
        ListView selectionList = (ListView) view.findViewById(R.id.selectionList);
        selectionList.setAdapter(new WebSelectionAdapter(getActivity()));
        selectionList.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0: // Local
                BusProvider.getInstance().post(new WebsiteSelectedEvent(WebsiteSelection.LOCAL));
                break;
            case 1: // Soundcloud
                BusProvider.getInstance().post(new WebsiteSelectedEvent(WebsiteSelection.SOUNDCLOUD));
                break;
        }
    }
}
