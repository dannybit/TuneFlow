package com.dannybit.tuneflow;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dannybit.tuneflow.fragments.PlaylistListFragment;
import com.dannybit.tuneflow.fragments.search.SearchSoundcloudFragment;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.models.SoundcloudSong;
import com.dannybit.tuneflow.network.SoundcloudRestClient;
import com.dannybit.tuneflow.views.adapters.SongAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchSongActivity extends ActionBarActivity implements SearchSoundcloudFragment.OnFragmentInteractionListener {

    private WebsiteSelection websiteSelection;
    private Toolbar toolbar;
    private SearchSoundcloudFragment searchSoundcloudFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_song);
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_search);
        websiteSelection = (WebsiteSelection) getIntent().getSerializableExtra("SELECTION");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        searchSoundcloudFragment = new SearchSoundcloudFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.search_container, searchSoundcloudFragment).commit();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        handleIntent(intent);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            songSearch(query);
        }
    }



    public void songSearch(String query){
        RequestParams params = new RequestParams();
        params.put("client_id", SoundcloudRestClient.CLIENT_ID);
        params.put("q", query);
        SoundcloudRestClient.get(params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                int length = response.length();
                searchSoundcloudFragment.getAdapter().clear();
                for (int i = 0; i < length; i++){
                    SoundcloudSong song = new SoundcloudSong();
                    try {
                        JSONObject jsonSong = response.getJSONObject(i);
                        song.setSoundcloudId(jsonSong.getString("id"));
                        song.setDuration(jsonSong.getString("duration"));
                        song.setTrackName(jsonSong.getString("title"));
                        song.setArtworkLink(jsonSong.getString("artwork_url"));
                        song.setUrl(jsonSong.getString("url"));
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    searchSoundcloudFragment.getAdapter().add(song);


                }
                searchSoundcloudFragment.getAdapter().notifyDataSetChanged();
                try {
                    Log.v("hello", response.getJSONObject(0).getString("title"));
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public void onFragmentInteraction(Song song) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", song);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_song, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_view).getActionView();
        ComponentName cn = new ComponentName(this, SearchSongActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("hello", "clicked");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                Log.v("HELLO", "back");
                finish();
             default:
                 return super.onOptionsItemSelected(item);
        }



    }
}
