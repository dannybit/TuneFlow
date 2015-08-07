package com.dannybit.tuneflow.fragments.search.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class SearchLocalSongsAdapter extends BaseAdapter {

    private ArrayList<LocalSong> songs;
    private Activity context;

    static class ViewHolder{
        public ImageView songArt;
        public TextView songName;
        public TextView songDuration;

    }

    public SearchLocalSongsAdapter(Activity context, ArrayList<LocalSong> songs){
        this.context = context;
        this.songs = songs;
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
        song.loadImage(context, viewHolder.songArt);
        viewHolder.songName.setText(song.getTrackName());
        viewHolder.songDuration.setText(song.getDurationInMins());
        return view;
    }

}
