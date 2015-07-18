package com.dannybit.tuneflow.models;

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

    public void setSoundcloudId(String soundcloudId){
        this.soundcloudId = soundcloudId;
    }

    public String getSoundcloudId(){
        return this.soundcloudId;
    }

    public void load(final SongAdapter adapter){
        RequestParams params = new RequestParams();
        params.put("client_id", SoundcloudRestClient.CLIENT_ID);
        SoundcloudRestClient.getTrack(getSoundcloudId(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Song song = new Song();
                try {
                    song.setTrackName(response.getString("title"));
                    song.setArtworkLink(response.getString("artwork_url"));
                    song.setDuration(response.getString("duration"));
                    song.setUrl(addClientIdToUrl(response.getString("stream_url")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.add(song);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                
            }
        });
    }

    public String addClientIdToUrl(String url){
        return url + "?client_id=" + SoundcloudRestClient.CLIENT_ID;
    }
}
