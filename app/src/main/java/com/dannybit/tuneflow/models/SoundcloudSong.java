package com.dannybit.tuneflow.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dannybit.tuneflow.network.SoundcloudRestClient;
import com.dannybit.tuneflow.views.adapters.SongAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danielnamdar on 7/18/15.
 */
public class SoundcloudSong extends Song {
    private String soundcloudId;

    public SoundcloudSong(){
        super();
    }

    public SoundcloudSong(Parcel in){
        super(in);
        soundcloudId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(soundcloudId);
    }

    public void setSoundcloudId(String soundcloudId){
        this.soundcloudId = soundcloudId;
    }

    public String getSoundcloudId(){
        return this.soundcloudId;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SoundcloudSong> CREATOR = new Parcelable.Creator<SoundcloudSong>() {
        @Override
        public SoundcloudSong createFromParcel(Parcel in) {
            return new SoundcloudSong(in);
        }

        @Override
        public SoundcloudSong[] newArray(int size) {
            return new SoundcloudSong[size];
        }
    };

    /*
    public void load(final SongAdapter adapter){
        RequestParams params = new RequestParams();
        params.put("client_id", SoundcloudRestClient.CLIENT_ID);
        SoundcloudRestClient.getTrack(getSoundcloudId(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    setTrackName(response.getString("title"));
                    setArtworkLink(response.getString("artwork_url"));
                    setDuration(response.getString("duration"));
                    setUrl(addClientIdToUrl(response.getString("stream_url")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.add(SoundcloudSong.this);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            }
        });
    }
    */

    public String addClientIdToUrl(String url){
        return url + "?client_id=" + SoundcloudRestClient.CLIENT_ID;
    }
}
