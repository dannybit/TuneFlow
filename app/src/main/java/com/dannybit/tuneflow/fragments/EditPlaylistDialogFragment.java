package com.dannybit.tuneflow.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.NewPlaylistCreatedEvent;
import com.dannybit.tuneflow.events.RenamePlaylistEvent;
import com.dannybit.tuneflow.models.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPlaylistDialogFragment extends DialogFragment {

    private EditText editName;

    public EditPlaylistDialogFragment() {
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
        getDialog().setTitle(R.string.edit_playlist_dialog_title);
        final Playlist playlistSelected = getArguments().getParcelable("PLAYLIST");
        View view = inflater.inflate(R.layout.fragment_edit_playlist_dialog, container, false);
        editName = (EditText) view.findViewById(R.id.editPlaylistName);
        Button renameButton = (Button) view.findViewById(R.id.bRename);
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editName.getText().toString();
                BusProvider.getInstance().post(new RenamePlaylistEvent(playlistSelected, newName));
                dismiss();
            }
        });
        return view;
    }


}
