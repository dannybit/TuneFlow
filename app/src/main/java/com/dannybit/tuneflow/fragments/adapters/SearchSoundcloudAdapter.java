package com.dannybit.tuneflow.fragments.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnamdar on 7/9/15.
 */
public class SearchSoundcloudAdapter extends BaseAdapter {

    private List<Song> songs;
    private Activity context;

    static class ViewHolder{
        public ImageView songArt;
        public TextView songName;
    }

    public SearchSoundcloudAdapter(Activity context){
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

    public void add(Song s){
        songs.add(s);
    }

    public void clear(){
        songs.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            LayoutInflater vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.search_soundcloud_row, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.songArt = (ImageView) view.findViewById(R.id.searchSongArtSoundcloud);
            viewHolder.songName = (TextView) view.findViewById(R.id.searchSongNameSoundcloud);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Song song = songs.get(position);
        song.loadImage(context, viewHolder.songArt);
       // Picasso.with(context).load(song.getArtworkLink()).error(R.drawable.soundcloud_icon).into(viewHolder.songArt);
        viewHolder.songName.setText(song.getTrackName());
        return view;
    }
}
