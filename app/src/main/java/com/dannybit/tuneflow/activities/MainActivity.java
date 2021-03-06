package com.dannybit.tuneflow.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import com.dannybit.tuneflow.BusProvider;
import com.dannybit.tuneflow.Utils.MainUtils;
import com.dannybit.tuneflow.events.AddPlaylistToQueueEvent;
import com.dannybit.tuneflow.events.DeletePlaylistEvent;
import com.dannybit.tuneflow.events.DeleteSongEvent;
import com.dannybit.tuneflow.events.NewPlaylistCreatedEvent;
import com.dannybit.tuneflow.events.PlaylistSelectedEvent;
import com.dannybit.tuneflow.events.RenamePlaylistEvent;
import com.dannybit.tuneflow.events.RepeatButtonClickedEvent;
import com.dannybit.tuneflow.events.SongSelectedEvent;
import com.dannybit.tuneflow.events.WebsiteSelectedEvent;
import com.dannybit.tuneflow.fragments.NavigationDrawerCallbacks;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.database.DatabaseHelper;
import com.dannybit.tuneflow.fragments.NavigationDrawerFragment;
import com.dannybit.tuneflow.fragments.NowPlayingFragment;
import com.dannybit.tuneflow.fragments.PlaylistListFragment;
import com.dannybit.tuneflow.fragments.SongsListFragment;
import com.dannybit.tuneflow.fragments.search.WebsiteSelection;
import com.dannybit.tuneflow.models.Playlist;
import com.dannybit.tuneflow.models.Song;
import com.dannybit.tuneflow.services.AudioPlaybackService;
import com.dannybit.tuneflow.views.notifications.NowPlayingNotification;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks,
        AudioPlaybackService.SongCompletedListener,
        NowPlayingFragment.OnMediaPlayerButtonClickedListener,
        AudioPlaybackService.SongPreparedListener
{

    public static final String TAG = MainActivity.class.getName();

    /* Used for ActivityResult when starting the SearchActivity */
    public static final int FIND_SOUNDClOUD_SONG_REQUEST = 1;
    public static final int FIND_LOCAL_SONG_REQUEST = 2;

    /* A notification id allows the app to modify the current running notification rather than starting a new one */
    public static final int PLAYING_SONG_NOTIFICATION_ID = 1;

    public static final int DRAWER_PLAYLISTS_POS = 0;
    public static final int DRAWER_SETTINGS_POS = 1;

    /* Notification Actions */
    public static final String PLAY_PAUSE_ACTION = "com.dannybit.PLAY_PAUSE_ACTION";
    public static final String LAUNCH_NOW_PLAYING_ACTION = "com.dannybit.LAUNCH_NOW_PLAYING_ACTION";
    public static final String BACKWARD_ACTION = "com.dannybit.BACKWARD_TRACK_ACTION";

    public static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST_FRAGMENT_TAG";
    public static final String SONGS_LIST_FRAGMENT_TAG = "SONGS_LIST_FRAGMENT_TAG";
    public static final String NOW_PLAYING_FRAGMENT_TAG = "NOW_PLAYING_FRAGMENT_TAG";


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

    private boolean nowPlayingFragmentExpanded;
    private boolean songSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Picasso.with(this).setIndicatorsEnabled(true);
        instance = this;
        dbHelper = DatabaseHelper.getInstance(this);

        /* Initial setup */
        setupSlidingPlayer();
        setupToolbar();
        setupDrawer();
        setupDraggableFragment();

        if (savedInstanceState == null) {
            // Create a new playlist fragment and add it to the activity
            startPlaylistsFragment();
        } else {
            // Restore fragment references lost at runtime change
            restorePlaylistsFragmentReference();
            restoreSongsListFragmentReference();
            restoreNowPlayingFragmentReference();

            // Restore variables lost at runtime change
            currentPlaylistId = savedInstanceState.getLong("CURRENT_PLAYLIST_ID");
            songSelected = savedInstanceState.getBoolean("SONG_SELECTED");

            // If a song was selected before runtime change, show the sliding player.
            if (songSelected){
                slidingUpPanelLayout.setPanelHeight((int) MainUtils.convertDpToPixel((int) getResources().getDimension(R.dimen.draggable_header) / getResources().getDisplayMetrics().density, this));
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
                Log.v(TAG, "Panel Collapsed");
                hidePlayOrPauseButton();
                nowPlayingFragmentExpanded = false;
            }

            @Override
            public void onPanelExpanded(View view) {
                Log.v(TAG, "Panel Expanded");
                showPlayOrPauseButton();
                nowPlayingFragmentExpanded = true;
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



    private void setupDraggableFragment(){
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


    private ServiceConnection audioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioPlaybackService.MusicBinder binder = (AudioPlaybackService.MusicBinder) iBinder;
            audioService = binder.getService();
            binder.setSongCompletedListener(MainActivity.this);
            binder.setSongPreparedListener(MainActivity.this);
            if (audioService.isPlaying()){
                openNowPlayingFragment(audioService.getCurrentSong(), true);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    private void setupToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }

    private void setupDrawer(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, drawerLayout, mToolbar);
    }



    private void restorePlaylistsFragmentReference(){
        playlistListFragment = (PlaylistListFragment) getSupportFragmentManager().findFragmentByTag(PLAYLIST_FRAGMENT_TAG);

    }

    private void restoreSongsListFragmentReference(){
        SongsListFragment songsListFragment = (SongsListFragment) getSupportFragmentManager().findFragmentByTag(SONGS_LIST_FRAGMENT_TAG);
        if (songsListFragment != null){
            currentSongsListFragment = songsListFragment;
        }
    }

    private void restoreNowPlayingFragmentReference(){
        NowPlayingFragment restoredNowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentByTag(NOW_PLAYING_FRAGMENT_TAG);
        if (restoredNowPlayingFragment != null){
            nowPlayingFragment = restoredNowPlayingFragment;
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == DRAWER_PLAYLISTS_POS){
            switchToPlaylistsFragment();

        }
        else if (position == DRAWER_SETTINGS_POS){
            switchToSettingsFragment();
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
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }
        else if (nowPlayingFragmentExpanded){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            nowPlayingFragmentExpanded = false;
        }
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


    @Subscribe
    public void onSongSelected(SongSelectedEvent songSelectedEvent) {
        audioService.newQueue(songSelectedEvent.getSongs(), songSelectedEvent.getPosition());
        // Only start the nowplayingfragment if there was no error setting the data source for the mediaplayer
        if (audioService.playSong()) {
            openNowPlayingFragment(audioService.getCurrentSong(), false);
            startNotification(audioService.getCurrentSong());
        }
        songSelected = true;
    }



    private void openNowPlayingFragment(Song song, boolean isPlaying){
        slidingUpPanelLayout.setPanelHeight((int) MainUtils.convertDpToPixel((int) getResources().getDimension(R.dimen.draggable_header) / getResources().getDisplayMetrics().density, this));
        if (nowPlayingFragment == null){
            nowPlayingFragment = new NowPlayingFragment();
            Bundle extras = new Bundle();
            extras.putParcelable("SONG", song);
            extras.putBoolean("IS_PLAYING", isPlaying);
            nowPlayingFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.sliding_container, nowPlayingFragment, NOW_PLAYING_FRAGMENT_TAG).commit();
        } else {
            updateNowPlayingFragment(song, isPlaying);
        }


    }

    private void updateNowPlayingFragment(Song song, boolean isPlaying){
        nowPlayingFragment.updateSong(song, isPlaying);
    }





    @Override
    public void songCompleted(Song nextSongToPlay) {
        startNotification(nextSongToPlay);
        nextSongNowPlayingFragment(nextSongToPlay);
    }

    private void nextSongNowPlayingFragment(Song nextSongToPlay){
        if (nowPlayingFragment != null){
            nowPlayingFragment.updateSong(nextSongToPlay, false);
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



    @Subscribe
    public void onPlaylistSelected(PlaylistSelectedEvent playlistSelectedEvent) {
       playlistSelected(playlistSelectedEvent.getPlaylist());
    }

    private void playlistSelected(Playlist playlist){
        currentPlaylistId = playlist.getId();
        currentSongsListFragment = new SongsListFragment();
        Bundle extras = new Bundle();
        extras.putParcelableArrayList("SONGS", playlist.getSongs());
        extras.putParcelable("PLAYLIST", playlist);
        currentSongsListFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, currentSongsListFragment, SONGS_LIST_FRAGMENT_TAG).addToBackStack(null).commit();
    }

    public void togglePlayOrPause(){
        getMusicService().togglePlayOrPause();
        startNotification(getMusicService().getCurrentSong());
    }



    @Subscribe
    public void onWebsiteSelected(WebsiteSelectedEvent websiteSelectedEvent) {
        WebsiteSelection websiteSelection = websiteSelectedEvent.getSelection();
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


    @Subscribe
    public void onNewPlaylistCreated(NewPlaylistCreatedEvent newPlaylistCreatedEvent) {
        Playlist playlist = newPlaylistCreatedEvent.getPlaylist();
        dbHelper.createPlaylist(playlist);
        playlistListFragment.addPlaylist(playlist);
        if (playlistListFragment != null) {
            playlistListFragment.scrollToBottom();
        }
    }

    @Subscribe
    public void onRenamePlaylist(RenamePlaylistEvent renamePlaylistEvent){
        Playlist playlist = renamePlaylistEvent.getPlaylist();
        String newName = renamePlaylistEvent.getNewName();
        dbHelper.renamePlaylist(playlist, newName);
        playlist.setName(newName);
        playlistListFragment.getAdapter().notifyDataSetChanged();
    }

    @Subscribe
    public void onDeletePlaylist(DeletePlaylistEvent deletePlaylistEvent){
        Playlist playlist = deletePlaylistEvent.getPlaylist();
        dbHelper.deletePlaylist(playlist);
        playlistListFragment.getAdapter().remove(playlist);
        playlistListFragment.getAdapter().notifyDataSetChanged();
    }

    @Subscribe
    public void onDeleteSong(DeleteSongEvent deleteSongEvent){
        Playlist playlist = deleteSongEvent.getPlaylist();
        Song songToDelete = deleteSongEvent.getSong();
        dbHelper.deleteSong(songToDelete, playlist);
        currentSongsListFragment.getAdapter().remove(songToDelete);
        currentSongsListFragment.getAdapter().notifyDataSetChanged();
        audioService.removeSong(songToDelete);
    }

    @Subscribe
    public void onAddPlaylistToQueue(AddPlaylistToQueueEvent addPlaylistToQueue){
        Playlist playlist = addPlaylistToQueue.getPlaylist();
       if (audioService.getSongQueue().isEmpty()){
           BusProvider.getInstance().post(new SongSelectedEvent(playlist.getSongs(), 0));
       } else {
            audioService.getSongQueue().addSongs(playlist.getSongs());
        }

    }

    @Subscribe
    public void onRepeatButtonClicked(RepeatButtonClickedEvent repeatButtonClickedEvent){
        if (audioService.getSongQueue().isRepeat()){
            audioService.getSongQueue().disableRepeat();
            nowPlayingFragment.disableRepeatButton();
        } else {
            audioService.getSongQueue().enableRepeat();
            nowPlayingFragment.enableRepeatButton();
        }

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

    private void startNotification(Song song){
        NowPlayingNotification nowPlayingNotification = new NowPlayingNotification(this);
        nowPlayingNotification.createNowPlayingNotification(song, getMusicService().isPlaying());
    }

    @Override
    public void onForwardClicked() {
        audioService.getSongQueue().disableRepeat();
        nowPlayingFragment.disableRepeatButton();
        startNotification(audioService.getCurrentSong());
    }

    @Override
    public void onBackwardClicked() {
        audioService.getSongQueue().disableRepeat();
        nowPlayingFragment.disableRepeatButton();
        startNotification(audioService.getCurrentSong());
    }

    public void notificationBackwardClicked(){
        audioService.playBackwardSong();
        onBackwardClicked();
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof NowPlayingFragment){
            nowPlayingFragment.updateSong(getMusicService().getCurrentSong(), false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("CURRENT_PLAYLIST_ID", currentPlaylistId);
        outState.putBoolean("SONG_SELECTED", songSelected);
        super.onSaveInstanceState(outState);
    }
}
