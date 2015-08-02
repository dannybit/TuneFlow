package com.dannybit.tuneflow.activities;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dannybit.tuneflow.Utils.MainUtils;
import com.dannybit.tuneflow.fragments.NavigationDrawerCallbacks;
import com.dannybit.tuneflow.R;
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
import com.dannybit.tuneflow.views.notifications.NowPlayingNotification;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks,
        SongsListFragment.OnSongSelectedListener,
        PlaylistListFragment.OnPlaylistSelectedListener,
        WebsiteSelectionDialogFragment.OnWebsiteSelectionListner,
        NewPlaylistDialogFragment.OnNewPlaylistCreatedListener,
        AudioPlaybackService.SongCompletedListener,
        NowPlayingFragment.OnMediaPlayerButtonClickedListener, AudioPlaybackService.SongPreparedListener{

    /* Used for ActivityResult when starting the SearchActivity */
    public static final int FIND_SOUNDClOUD_SONG_REQUEST = 1;
    public static final int FIND_LOCAL_SONG_REQUEST = 2;

    /* A notification id allows the app to modify the current running notification rather than starting a new one */
    public static final int PLAYING_SONG_NOTIFICATION_ID = 1;

    public static final int DRAWER_NOW_PLAYING_POS = 0;
    public static final int DRAWER_PLAYLISTS_POS = 1;
    public static final int DRAWER_SETTINGS_POS = 2;

    /* Notification Actions */
    public static final String PLAY_PAUSE_ACTION = "com.dannybit.PLAY_PAUSE_ACTION";
    public static final String LAUNCH_NOW_PLAYING_ACTION = "com.dannybit.LAUNCH_NOW_PLAYING_ACTION";
    public static final String BACKWARD_ACTION = "com.dannybit.BACKWARD_TRACK_ACTION";
    public static final String SWITCH_TO_NOW_PLAYING_ACTION = "com.dannybit.START_NOW_PLAYING_ACTION";

    public static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST_FRAGMENT_TAG";
    public static final String SONGS_LIST_FRAGMENT_TAG = "SONGS_LIST_FRAGMENT_TAG";
    public static final String NOW_PLAYING_FRAGMENT_TAG = "NOW_PLAYING_FRAGMENT_TAG";

    private boolean startFromNotification;

    private Toolbar mToolbar;
    private DatabaseHelper dbHelper;


    private long currentPlaylistId;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    /* Fragments */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private PlaylistListFragment playlistListFragment;
    private SongsListFragment currentSongsListFragment;
    private NowPlayingFragment nowPlayingFragment;

    private AudioPlaybackService audioService;
    private Intent playIntent;

    private DrawerLayout drawerLayout;
    public static MainActivity instance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        dbHelper = DatabaseHelper.getInstance(this);

        /* Initial setup */
        setupSlidingPlayer();
        initToolbar();
        setupDrawer();
        setupDragableFragment();

        if (getIntent().getAction().equals(SWITCH_TO_NOW_PLAYING_ACTION)){
            startFromNotification = true;
        } else {

            if (isNetworkAvailable()) {

                if (savedInstanceState == null) {
                    startPlaylistsFragment();
                } else {
                    restorePlaylistsFragment();
                }

            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG);
            }

        }

        if (currentSongsListFragment == null){
            restoreSongsListFragment();
        }
        if (savedInstanceState != null) {
            currentPlaylistId = savedInstanceState.getLong("CURRENT_PLAYLIST_ID");
            NowPlayingFragment restoredNowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentByTag(NOW_PLAYING_FRAGMENT_TAG);
            if (restoredNowPlayingFragment != null){
                nowPlayingFragment = restoredNowPlayingFragment;
            }
        }

    }

    private void setupSlidingPlayer(){
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                hidePlayOrPauseButton();
            }

            @Override
            public void onPanelExpanded(View view) {
                showPlayOrPauseButton();
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
    }

    private void startPlaylistsFragment(){
        playlistListFragment = new PlaylistListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, playlistListFragment, PLAYLIST_FRAGMENT_TAG).commit();
    }

    private void showPlayOrPauseButton(){
        nowPlayingFragment.hideSlidingPlayOrPauseButton();
    }

    private void hidePlayOrPauseButton(){
        nowPlayingFragment.showSlidingPlayOrPauseButton();
    }



    private void setupDragableFragment(){
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelHeight(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null){
            playIntent = new Intent(this, AudioPlaybackService.class);
            startService(playIntent);
            bindService(playIntent, audioConnection, Context.BIND_AUTO_CREATE);

        }

    }


    private ServiceConnection audioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioPlaybackService.MusicBinder binder = (AudioPlaybackService.MusicBinder) iBinder;
            audioService = binder.getService();
            binder.setSongCompletedListener(MainActivity.this);
            binder.setSongPreparedListener(MainActivity.this);


            if (getIntent().getAction().equals(SWITCH_TO_NOW_PLAYING_ACTION)){
                if (nowPlayingFragment != null) {
                    switchToNowPlayingFragmentFromNotification();
                } else {
                    nowPlayingFragment = new NowPlayingFragment();

                    startNowPlaylingFragmentFromNotification(audioService.getCurrentSong());
                }
            }

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



    private void restorePlaylistsFragment(){
        playlistListFragment = (PlaylistListFragment) getSupportFragmentManager().findFragmentByTag(PLAYLIST_FRAGMENT_TAG);

    }

    private void restoreSongsListFragment(){
        SongsListFragment songsListFragment = (SongsListFragment) getSupportFragmentManager().findFragmentByTag(SONGS_LIST_FRAGMENT_TAG);
        if (songsListFragment != null){
            currentSongsListFragment = songsListFragment;
        }
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

    private void switchToNowPlayingFragmentFromNotification(){
        if (nowPlayingFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, nowPlayingFragment).commit();
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
                if (startFromNotification){
                    startFromNotification = false;
                    getSupportActionBar().show();
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.container)).commit();
                    startPlaylistsFragment();
                } else {
                    super.onBackPressed();
                }
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
            openNowPlayingFragment(audioService.getCurrentSong());
            startNotification(audioService.getCurrentSong());
        }


    }

    private void openNowPlayingFragment(Song song){
        slidingUpPanelLayout.setPanelHeight((int) MainUtils.convertDpToPixel((int) getResources().getDimension(R.dimen.draggable_header) / getResources().getDisplayMetrics().density, this));
        if (nowPlayingFragment == null){
            nowPlayingFragment = new NowPlayingFragment();
            Bundle extras = new Bundle();
            extras.putParcelable("SONG", song);
            nowPlayingFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.sliding_container, nowPlayingFragment, NOW_PLAYING_FRAGMENT_TAG).commit();
        } else {
            updateNowPlayingFragment(song);
        }


    }

    private void updateNowPlayingFragment(Song song){
        nowPlayingFragment.updateSong(song);
    }





    @Override
    public void songCompleted(Song nextSongToPlay) {
        startNotification(nextSongToPlay);
        nextSongNowPlayingFragment(nextSongToPlay);
    }

    private void nextSongNowPlayingFragment(Song nextSongToPlay){
        if (nowPlayingFragment != null){
            nowPlayingFragment.updateSong(nextSongToPlay);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.container, nowPlayingFragment, NOW_PLAYING_FRAGMENT_TAG).addToBackStack(null).commit();
    }

    private void startNowPlaylingFragmentFromNotification(Song song){
        if (nowPlayingFragment == null){
            nowPlayingFragment = new NowPlayingFragment();
        }
        Bundle extras = new Bundle();
        extras.putParcelable("SONG", song);
        nowPlayingFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, nowPlayingFragment).commit();
    }

    @Override
    public void onPlaylistSelected(Playlist playlist) {
        currentPlaylistId = playlist.getId();
        currentSongsListFragment = new SongsListFragment();
        Bundle extras = new Bundle();
        extras.putParcelableArrayList("SONGS", playlist.getSongs());
        extras.putString("PLAYLIST_NAME", playlist.getName());
        currentSongsListFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, currentSongsListFragment, SONGS_LIST_FRAGMENT_TAG).addToBackStack(null).commit();
    }

    public void togglePlayOrPause(){
        getMusicService().togglePlayOrPause();
        startNotification(getMusicService().getCurrentSong());
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
        dbHelper.createPlaylistSong(currentPlaylistId, addedSong.getId());
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
        if (audioService != null){
            unbindService(audioConnection);
        }
        super.onDestroy();
    }

    @Override
    public void songPrepared(Song song) {
        nowPlayingFragment.enableProgressBar();
        startNotification(getMusicService().getCurrentSong());
    }

    private boolean isNetworkAvailable(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void startNotification(Song song){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NowPlayingNotification nowPlayingNotification = new NowPlayingNotification(this);
        nowPlayingNotification.createNowPlayingNotification(song, getMusicService().isPlaying());
    }


    @Override
    public void onForwardClicked() {
        startNotification(audioService.getCurrentSong());
    }

    @Override
    public void onBackwardClicked() {
        startNotification(audioService.getCurrentSong());
    }

    public void notificationBackwardClicked(){
        audioService.playBackwardSong();
        onBackwardClicked();
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof NowPlayingFragment){
            nowPlayingFragment.updateSong(getMusicService().getCurrentSong());
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("CURRENT_PLAYLIST_ID", currentPlaylistId);
        super.onSaveInstanceState(outState);
    }
}
