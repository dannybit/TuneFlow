package com.dannybit.tuneflow.fragments.search.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.Utils.MainUtils;
import com.dannybit.tuneflow.models.Artist;
import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class SearchLocalArtistsAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Artist> allArtists;
    private ArrayList<Artist> artists;
    private Activity context;
    private String query;

    static class ViewHolder{
        public ImageView artistArt;
        public TextView artistName;
        public TextView artistNumberOfSongs;
    }

    public SearchLocalArtistsAdapter(Activity context, ArrayList<Artist> artists){
        this.context = context;
        this.artists = artists;
        this.allArtists = artists;
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
        if (query != null) {
            MainUtils.highlightText(artist.getArtistName(), query, viewHolder.artistName);
        } else {
            viewHolder.artistName.setText(artist.getArtistName());
        }
        viewHolder.artistNumberOfSongs.setText(String.valueOf(artist.getSongCount()));
        return view;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                artists = (ArrayList<Artist>) results.values;
                query = constraint.toString();
                notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.toString().isEmpty()){

                    results.count = allArtists.size();
                    results.values = allArtists;
                    return results;
                }
                ArrayList<Artist> filteredLocalSongs = new ArrayList<Artist>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < allArtists.size(); i++) {
                    if (allArtists.get(i).getArtistName().toLowerCase().contains(constraint.toString()))  {
                        filteredLocalSongs.add(allArtists.get(i));
                    }
                }

                results.count = filteredLocalSongs.size();
                results.values = filteredLocalSongs;

                return results;
            }
        };

        return filter;

    }

}


