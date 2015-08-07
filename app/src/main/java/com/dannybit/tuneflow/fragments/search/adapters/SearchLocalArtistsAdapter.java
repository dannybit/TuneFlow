package com.dannybit.tuneflow.fragments.search.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.models.Artist;
import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class SearchLocalArtistsAdapter extends BaseAdapter {

    private ArrayList<Artist> artists;
    private Activity context;

    static class ViewHolder{
        public ImageView artistArt;
        public TextView artistName;
        public TextView artistNumberOfSongs;
    }

    public SearchLocalArtistsAdapter(Activity context, ArrayList<Artist> artists){
        this.context = context;
        this.artists = artists;
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return artists.get(position);
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
            view = vi.inflate(R.layout.artist_row, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.artistArt = (ImageView) view.findViewById(R.id.artistArt);
            viewHolder.artistName = (TextView) view.findViewById(R.id.artistName);
            viewHolder.artistNumberOfSongs = (TextView) view.findViewById(R.id.artistNumberOfSongs);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Artist artist = artists.get(position);
        //artist.loadImage(context, viewHolder.artistArt);
        viewHolder.artistName.setText(artist.getArtistName());
        viewHolder.artistNumberOfSongs.setText(String.valueOf(artist.getSongCount()));
        return view;
    }

}


