package com.dannybit.tuneflow.fragments.search.adapters;

import android.app.Activity;
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
import com.dannybit.tuneflow.models.Album;
import com.dannybit.tuneflow.models.Artist;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 8/7/15.
 */
public class SearchLocalAlbumsAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Album> albums;
    private ArrayList<Album> allAlbums;
    private Activity context;
    private String query;

    static class ViewHolder{
        public ImageView albumArt;
        public TextView albumName;
        public TextView albumNumberOfSongs;

    }

    public SearchLocalAlbumsAdapter(Activity context, ArrayList<Album> albums){
        this.context = context;
        this.albums = albums;
        this.allAlbums = albums;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
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
            view = vi.inflate(R.layout.album_row, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.albumArt = (ImageView) view.findViewById(R.id.albumArt);
            viewHolder.albumName = (TextView) view.findViewById(R.id.albumName);
            viewHolder.albumNumberOfSongs = (TextView) view.findViewById(R.id.albumNumberOfSongs);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Album album = albums.get(position);
        album.loadImage(context, viewHolder.albumArt);
        if (query != null) {
            MainUtils.highlightText(album.getName(), query, viewHolder.albumName);
        } else {
            viewHolder.albumName.setText(album.getName());
        }
        viewHolder.albumNumberOfSongs.setText(String.valueOf(album.getSongCount()));
        return view;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                albums = (ArrayList<Album>) results.values;
                query = constraint.toString();
                notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.toString().isEmpty()){

                    results.count = allAlbums.size();
                    results.values = allAlbums;
                    return results;
                }
                ArrayList<Album> filteredLocalSongs = new ArrayList<Album>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < allAlbums.size(); i++) {
                    if (allAlbums.get(i).getName().toLowerCase().contains(constraint.toString()))  {
                        filteredLocalSongs.add(allAlbums.get(i));
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
