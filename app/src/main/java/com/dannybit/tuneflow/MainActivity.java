package com.dannybit.tuneflow;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dannybit.tuneflow.database.DatabaseHelper;
import com.dannybit.tuneflow.fragments.NavigationDrawerFragment;
import com.dannybit.tuneflow.fragments.NewPlaylistDialogFragment;
import com.dannybit.tuneflow.fragments.NowPlayingFragment;
import com.dannybit.tuneflow.fragments.PlaylistListFragment;
import com.dannybit.tuneflow.fragments.SongsListFragment;
import com.dannybit.tuneflow.fragments.WebsiteSelectionDialogFragment;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.services.AudioPlaybackService;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks,
        SongsListFragment.OnSongSelectedListener,
        PlaylistListFragment.OnPlaylistSelectedListener,
        WebsiteSelectionDialogFragment.OnWebsiteSelectionListner,
        NewPlaylistDialogFragment.OnNewPlaylistCreatedListener,
        AudioPlaybackService.SongCompletedListener{

    /* Used for ActivityResult when starting the SearchActivity */
    public static final int FIND_SOUNDClOUD_SONG_REQUEST = 1;
    public static final int FIND_LOCAL_SONG_REQUEST = 2;

    /* A notification id allows the app to modify the current running notification rather than starting a new one */
    public static final int PLAYING_SONG_NOTIFICATION_ID = 1;

    public static final int DRAWER_NOW_PLAYING_POS = 0;
    public static final int DRAWER_PLAYLISTS_POS = 1;
    public static final int DRAWER_SETTINGS_POS = 2;


    private Toolbar mToolbar;
    private DatabaseHelper dbHelper;

    private Playlist currentPlaylist;

    /* Fragments */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private PlaylistListFragment playlistListFragment;
    private SongsListFragment currentSongsListFragment;
    private NowPlayingFragment nowPlayingFragment;

    private AudioPlaybackService audioService;
    private Intent playIntent;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        setupDrawer();
        if (savedInstanceState == null) {
            startPlaylistsFragment();
        }
        dbHelper = DatabaseHelper.getInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null){
            playIntent = new Intent(this, AudioPlaybackService.class);
            bindService(playIntent, audioConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    private ServiceConnection audioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioPlaybackService.MusicBinder binder = (AudioPlaybackService.MusicBinder) iBinder;
            audioService = binder.getService();
            binder.setListener(MainActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }

    private void setupDrawer(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, drawerLayout, mToolbar);
    }

    private void startPlaylistsFragment(){
        playlistListFragment = new PlaylistListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, playlistListFragment).commit();
    }

    /* Used by NowPlayingFragment to disable the drawer */
    public void disableDrawer(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == DRAWER_NOW_PLAYING_POS){
            switchToNowPlayingFragment();
        }
        else if (position == DRAWER_PLAYLISTS_POS){
            switchToPlaylistsFragment();

        }
        else if (position == DRAWER_SETTINGS_POS){
            switchToSettingsFragment();
        }
    }

    private void switchToNowPlayingFragment(){
        if (nowPlayingFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, nowPlayingFragment).addToBackStack(null).commit();
        }
    }

    private void switchToPlaylistsFragment(){
        if (playlistListFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, playlistListFragment).commit();
        }
    }

    private void switchToSettingsFragment(){

    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {
            FragmentManager fm = getSupportFragmentManager();

            if (fm.getBackStackEntryCount() > 0){
                fm.popBackStack();
            }
            else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return false;
    }


    @Override
    public void onSongSelected(ArrayList<Song> songs, int position) {
        audioService.setList(songs);
        audioService.setSongPosition(position);
        // Only start the nowplayingfragment if there was no error setting the data source for the mediaplayer
        if (audioService.playSong()) {
            startNowPlayingFragment(audioService.getCurrentSong());
            startNotification(audioService.getCurrentSong());
        }
    }

    @Override
    public void songCompleted(Song nextSongToPlay) {
        nextSongNowPlayingFragment(nextSongToPlay);
    }

    private void nextSongNowPlayingFragment(Song nextSongToPlay){
        if (nowPlayingFragment != null){
            nowPlayingFragment.nextSong(nextSongToPlay);
        } else {
            startNowPlayingFragment(nextSongToPlay);
        }
    }

    private void startNowPlayingFragment(Song song){
        if (nowPlayingFragment == null){
            nowPlayingFragment = new NowPlayingFragment();
        }
        Bundle extras = new Bundle();
        extras.putParcelable("SONG", song);
        nowPlayingFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, nowPlayingFragment).addToBackStack(null).commit();
    }

    @Override
    public void onPlaylistSelected(Playlist playlist) {
        currentPlaylist = playlist;
        SongsListFragment songsListFragment = new SongsListFragment();
        Bundle extras = new Bundle();
        extras.putParcelableArrayList("SONGS", playlist.getSongs());
        extras.putString("PLAYLIST_NAME", playlist.getName());
        songsListFragment.setArguments(extras);
        currentSongsListFragment = songsListFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, songsListFragment).addToBackStack(null).commit();
    }



    @Override
    public void onWebsiteSelected(WebsiteSelection websiteSelection) {
        if (websiteSelection.equals(WebsiteSelection.SOUNDCLOUD)){
            startSoundcloudSearch();
        }
        else if (websiteSelection.equals(WebsiteSelection.LOCAL)){
            startLocalSearch();
        }
    }

    public void startSoundcloudSearch(){
        currentSongsListFragment.closeSelectionDialog();
        Intent intent = new Intent(this, SearchSongActivity.class);
        intent.putExtra("SELECTION", WebsiteSelection.SOUNDCLOUD);
        startActivityForResult(intent, FIND_SOUNDClOUD_SONG_REQUEST);

    }

    public void startLocalSearch(){
        currentSongsListFragment.closeSelectionDialog();
        Intent intent = new Intent(this, SearchSongActivity.class);
        intent.putExtra("SELECTION", WebsiteSelection.LOCAL);
        startActivityForResult(intent, FIND_LOCAL_SONG_REQUEST);
    }

    public void setActionBarTitle(String newTitle){
        setTitle(newTitle);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FIND_SOUNDClOUD_SONG_REQUEST){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    Song addedSong = data.getParcelableExtra("result");
                    if (addedSong != null) {
                        addNewSong(addedSong);
                    }
                }
            }
        }

        else if (requestCode == FIND_LOCAL_SONG_REQUEST){
            if (data != null) {
                Song addedSong = data.getParcelableExtra("result");
                if (addedSong != null) {
                    addNewSong(addedSong);
                }
            }

        }

    }

    private void addNewSong(Song addedSong){
        dbHelper.createSong(addedSong);
        dbHelper.createPlaylistSong(currentPlaylist.getId(), addedSong.getId());
        currentPlaylist.add(addedSong);
        currentSongsListFragment.addSongToList(addedSong);

    }


    @Override
    public void onNewPlaylistCreated(Playlist playlist) {
        dbHelper.createPlaylist(playlist);
        playlistListFragment.addPlaylist(playlist);
        onPlaylistSelected(playlist);
    }

    public AudioPlaybackService getMusicService(){
        return audioService;
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        audioService=null;
        super.onDestroy();
    }


    private void startNotification(Song song){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this).setContentTitle(song.getTrackName()).setSmallIcon(R.drawable.soundcloud_icon).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(PLAYING_SONG_NOTIFICATION_ID, notification);
    }

}
