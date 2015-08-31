package com.dannybit.tuneflow.fragments.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.views.SquareImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnamdar on 7/4/15.
 */
public class PlaylistAdapter extends BaseAdapter {
    private List<Playlist> playlists;
    private Activity context;

    static class ViewHolder{
        public SquareImageView playlistArt;
        public TextView playlistName;
       // public TextView playlistSize;

    }

    public PlaylistAdapter(Activity context){
        this.context = context;
        playlists = new ArrayList<Playlist>();

    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int position) {
        return playlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            LayoutInflater vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.playlist_item_grid, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.playlistArt = (SquareImageView) view.findViewById(R.id.playlistArtGridItem);
            viewHolder.playlistName = (TextView) view.findViewById(R.id.playlistNameGridItem);
            //viewHolder.playlistSize = (TextView) view.findViewById(R.id.playlistSize);
            view.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final Playlist playlist = playlists.get(position);

        if (playlist.getSize() > 0) {
            playlist.getSongs().get(0).loadImage(context, viewHolder.playlistArt);
        } else {
           viewHolder.playlistArt.setImageResource(R.drawable.web_hi_res_512);
        }
        viewHolder.playlistName.setText(playlist.getName());
        //viewHolder.playlistSize.setText(String.format("%d tunes", playlist.getSize()));
        return view;
    }

    public void add(Playlist playlist){
        playlists.add(playlist);
    }

    public void remove(Playlist playlist){
        playlists.remove(playlist);
    }
}


