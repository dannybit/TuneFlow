package com.dannybit.tuneflow.fragments;

import android.app.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.NewPlaylistCreatedEvent;
import com.dannybit.tuneflow.models.Playlist;

/**
 * Created by danielnamdar on 7/20/15.
 */
public class NewPlaylistDialogFragment extends DialogFragment {

    private EditText editName;


    public NewPlaylistDialogFragment() {
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

        getDialog().setTitle(R.string.new_playlist_dialog_title);
        View view = inflater.inflate(R.layout.fragment_new_edit_playlist_dialog, container, false);
        editName = (EditText) view.findViewById(R.id.playlistName);
        Button addButton = (Button) view.findViewById(R.id.bAddOrEdit);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Playlist playlist = new Playlist(editName.getText().toString());
                    BusProvider.getInstance().post(new NewPlaylistCreatedEvent(playlist));
                    dismiss();
            }
        });
        return view;
    }

}
