package com.dannybit.tuneflow.fragments.adapters;

import android.app.Activity;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;
import com.dannybit.tuneflow.models.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by danielnamdar on 9/4/15.
 */
public class WebSelectionAdapter extends BaseAdapter {

    private WebsiteSelection[] selections;
    private Activity context;

    static class ViewHolder {
        public TextView selectionText;
        public ImageView selectionImage;
    }

    public WebSelectionAdapter(Activity context){
        this.context = context;
        selections = WebsiteSelection.values();

    }

    @Override
    public int getCount() {
        return selections.length;
    }

    @Override
    public Object getItem(int position) {
        return selections[position];
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
            view = vi.inflate(R.layout.selection_row, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.selectionImage = (ImageView) view.findViewById(R.id.selectionImage);
            viewHolder.selectionText = (TextView) view.findViewById(R.id.selectionText);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        WebsiteSelection selection = selections[position];
        viewHolder.selectionText.setText(selection.toString());
        switch (selection){
            case LOCAL:
                Picasso.with(context).load(R.drawable.web_hi_res_512).into(viewHolder.selectionImage);
                break;
            case SOUNDCLOUD:
                Picasso.with(context).load(R.drawable.soundcloud_selection).into(viewHolder.selectionImage);
                break;
        }
        return view;
    }
}
