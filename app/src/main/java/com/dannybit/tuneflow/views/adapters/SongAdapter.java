package com.dannybit.tuneflow.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 6/27/15.
 */
public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private Activity context;

    static class ViewHolder{
        public ImageView songArt;
        public TextView songName;
        public TextView songDuration;

    }

    public SongAdapter(Activity context){
        this.context = context;
        songs = new ArrayList<Song>();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            LayoutInflater vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.song_row, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.songArt = (ImageView) view.findViewById(R.id.songArt);
            viewHolder.songName = (TextView) view.findViewById(R.id.songName);
            viewHolder.songDuration = (TextView) view.findViewById(R.id.songDuration);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Song song = songs.get(position);
        Picasso.with(context).load(song.getArtworkLink()).error(R.drawable.soundcloud_icon).into(viewHolder.songArt);
        viewHolder.songName.setText(song.getTrackName());
        viewHolder.songDuration.setText(song.getDurationInMins());
        return view;
    }

    public void add(Song s){
        songs.add(s);
    }
}
