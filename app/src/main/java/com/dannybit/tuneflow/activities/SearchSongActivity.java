package com.dannybit.tuneflow.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.VisibilityPropagation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.events.LocalAlbumClickedEvent;
import com.dannybit.tuneflow.events.LocalArtistClickedEvent;
import com.dannybit.tuneflow.events.LocalSongClickedEvent;
import com.dannybit.tuneflow.fragments.search.SearchLocalFragment;
import com.dannybit.tuneflow.fragments.search.SearchLocalSongsListFragment;
import com.dannybit.tuneflow.fragments.search.SearchSoundcloudFragment;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;
import com.dannybit.tuneflow.models.LocalSong;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.models.SoundcloudSong;
import com.dannybit.tuneflow.network.SoundcloudRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;


public class SearchSongActivity extends ActionBarActivity implements SearchSoundcloudFragment.OnFragmentInteractionListener {

    public static final String TAG = SearchSongActivity.class.getName();
    private WebsiteSelection websiteSelection;
    private Toolbar toolbar;
    private SearchSoundcloudFragment searchSoundcloudFragment;
    private SearchLocalFragment searchLocalFragment;
    private static final String SEARCH_LIST_SOUNDCLOUD_FRAGMENT_TAG = "SEARCH_LIST_SOUNDCLOUD_FRAGMENT";
    private static final String SEARCH_LIST_LOCAL_FRAGMENT_TAG = "SEARCH_LIST_LOCAL_FRAGMENT";
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_song);
        setupToolbar();

        if (savedInstanceState == null) {
            websiteSelection = (WebsiteSelection) getIntent().getSerializableExtra("SELECTION");
        }
        else {
            websiteSelection = (WebsiteSelection) savedInstanceState.getSerializable("SELECTION");
        }

        if (savedInstanceState == null) {
            if (websiteSelection.equals(WebsiteSelection.SOUNDCLOUD)) {
                searchSoundcloudFragment = new SearchSoundcloudFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.search_container, searchSoundcloudFragment, SEARCH_LIST_SOUNDCLOUD_FRAGMENT_TAG).commit();
            }
            else if (websiteSelection.equals(WebsiteSelection.LOCAL)){
                searchLocalFragment = new SearchLocalFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.search_container, searchLocalFragment, SEARCH_LIST_LOCAL_FRAGMENT_TAG).commit();
            }

            /*
            Intent intent = getIntent();
            handleIntent(intent);
            */
        } else {
            restoreSearchSoundcloudFragmentReference();
            restoreSearchLocalFragmentReference();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("SELECTION", websiteSelection);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);

    }

    private void restoreSearchSoundcloudFragmentReference(){
        searchSoundcloudFragment = (SearchSoundcloudFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_LIST_SOUNDCLOUD_FRAGMENT_TAG);
    }

    private void restoreSearchLocalFragmentReference(){
        searchLocalFragment = (SearchLocalFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_LIST_LOCAL_FRAGMENT_TAG);
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (websiteSelection.equals(WebsiteSelection.SOUNDCLOUD)) {
                soundcloudSongSearch(query);
            }
            else if (websiteSelection.equals(WebsiteSelection.LOCAL)){
                localSongSearch(query);
            }
        }
    }

    public void performSearch(String query){
        if (websiteSelection.equals(WebsiteSelection.SOUNDCLOUD)) {
            soundcloudSongSearch(query);
        }
        else if (websiteSelection.equals(WebsiteSelection.LOCAL)){
            localSongSearch(query);
        }
    }

    public void localSongSearch(String query){
        if (searchLocalFragment != null){
            searchLocalFragment.performQuery(query);
        }

    }

    public void soundcloudSongSearch(String query){
        Log.v(TAG, "souncdloudsearch");
        RequestParams params = new RequestParams();
        params.put("client_id", SoundcloudRestClient.CLIENT_ID);
        params.put("q", query);
        params.put("limit", 200);

        SoundcloudRestClient.get(params, new JsonHttpResponseHandler() {
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

                for (int i = 0; i < length; i++) {

                    SoundcloudSong song = new SoundcloudSong();
                    try {
                        JSONObject jsonSong = response.getJSONObject(i);
                        song.setSoundcloudId(jsonSong.getString("id"));
                        song.setDuration(jsonSong.getString("duration"));
                        song.setTrackName(jsonSong.getString("title"));
                        song.setArtworkLink(jsonSong.getString("artwork_url"));
                        song.setUrl(addClientIdToUrl(jsonSong.getString("stream_url")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    searchSoundcloudFragment.getAdapter().add(song);


                }
                searchSoundcloudFragment.getAdapter().notifyDataSetChanged();

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

    @Subscribe
    public void onLocalSongClicked(LocalSongClickedEvent localSongClickedEvent){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", localSongClickedEvent.getSong());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Subscribe
    public void onLocalArtistClicked(LocalArtistClickedEvent localArtistClickedEvent){
        SearchLocalSongsListFragment searchLocalSongsListFragment = new SearchLocalSongsListFragment();
        Bundle args = new Bundle();
        args.putParcelable("ARTIST", localArtistClickedEvent.getArtist());
        searchLocalSongsListFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_container, searchLocalSongsListFragment).addToBackStack(null).commit();
        menu.findItem(R.id.search_view).setVisible(false);
    }

    @Subscribe
    public void onLocalAlbumClicked(LocalAlbumClickedEvent localAlbumClickedEvent){
        SearchLocalSongsListFragment searchLocalSongsListFragment = new SearchLocalSongsListFragment();
        Bundle args = new Bundle();
        args.putParcelable("ALBUM", localAlbumClickedEvent.getAlbum());
        searchLocalSongsListFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_container, searchLocalSongsListFragment).addToBackStack(null).commit();
        menu.findItem(R.id.search_view).setVisible(false);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_song, menu);
        // Associate searchable configuration with the SearchView
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                performSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                performSearch(s);
                return true;
            }
        });
        searchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
             default:
                 return super.onOptionsItemSelected(item);
        }



    }

    public String addClientIdToUrl(String url){
        return url + "?client_id=" + SoundcloudRestClient.CLIENT_ID;
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportFragmentManager().popBackStack();
            menu.findItem(R.id.search_view).setVisible(true);
        } else {
            super.onBackPressed();
        }
    }
}
