package com.dannybit.tuneflow.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dannybit.tuneflow.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by danielnamdar on 7/21/15.
 */
public class LocalSong extends Song {

    public LocalSong(){
        songType = SongType.LOCAL;
    }
    public LocalSong(Parcel in){
        super(in);
        songType = SongType.LOCAL;
    }



    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LocalSong> CREATOR = new Parcelable.Creator<LocalSong>() {
        @Override
        public LocalSong createFromParcel(Parcel in) {
            return new LocalSong(in);
        }

        @Override
        public LocalSong[] newArray(int size) {
            return new LocalSong[size];
        }
    };

    @Override
    public void loadImage(Context context, ImageView imageView) {
        if (getArtworkLink() != null) {
            Picasso.with(context).load(new File(getArtworkLink())).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.web_hi_res_512).into(imageView);
        }
    }


    @Override
    public void loadThumbnail(Context context, ImageView imageView) {
        if (getArtworkLink() != null) {
            Picasso.with(context).load(new File(getArtworkLink())).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.web_hi_res_512).into(imageView);
        }
    }

    @Override
    public void styleTag(Context context, TextView tagText) {
        tagText.setText(R.string.local_tag_text);
        tagText.setTextColor(context.getResources().getColor(R.color.local_tag_color));
    }
}
