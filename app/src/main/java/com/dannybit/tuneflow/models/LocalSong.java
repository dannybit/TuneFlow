package com.dannybit.tuneflow.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by danielnamdar on 7/21/15.
 */
public class LocalSong extends Song {

    public LocalSong(){

    }
    public LocalSong(Parcel in){
        super(in);
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
            // load default image
        }
    }


    @Override
    public void loadThumbnail(Context context, ImageView imageView) {
        Picasso.with(context).load(new File(getArtworkLink())).into(imageView);
    }



}
