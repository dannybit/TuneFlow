package com.dannybit.tuneflow.fragments.search.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
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
import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by danielnamdar on 8/6/15.
 */
public class SearchLocalSongsAdapter extends BaseAdapter implements Filterable {

    private ArrayList<LocalSong> allSongs;
    private ArrayList<LocalSong> songs;
    private Activity context;
    private String query;

    static class ViewHolder{
        public ImageView songArt;
        public TextView songName;
        public TextView songDuration;

    }

    public SearchLocalSongsAdapter(Activity context, ArrayList<LocalSong> songs){
        this.context = context;
        this.songs = songs;
        this.allSongs = songs;
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
        if (query != null) {
            highlightText(song.getTrackName(), query, viewHolder.songName);
        } else {
            viewHolder.songName.setText(song.getTrackName());
        }
        viewHolder.songDuration.setText(song.getDurationInMins());
        return view;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                songs = (ArrayList<LocalSong>) results.values;
                query = constraint.toString();
                notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.v("HELLO", constraint.toString());
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.toString().isEmpty()){

                    results.count = allSongs.size();
                    results.values = allSongs;
                    return results;
                }
                ArrayList<LocalSong> filteredLocalSongs = new ArrayList<LocalSong>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < allSongs.size(); i++) {
                    if (allSongs.get(i).getTrackName().toLowerCase().contains(constraint.toString()))  {
                        filteredLocalSongs.add(allSongs.get(i));
                    }
                }

                results.count = filteredLocalSongs.size();
                results.values = filteredLocalSongs;

                return results;
            }
        };

        return filter;

    }

    private void highlightText(String originalValue, String filter, TextView textView){
        int startPos = originalValue.toLowerCase(Locale.US).indexOf(filter.toLowerCase(Locale.US));
        int endPos = startPos + filter.length();

        if (startPos != -1){
            Spannable spannable = new SpannableString(originalValue);
            ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {}}, new int[] { Color.BLUE });
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannable);
        }
        else {
            textView.setText(originalValue);
        }
    }
}
