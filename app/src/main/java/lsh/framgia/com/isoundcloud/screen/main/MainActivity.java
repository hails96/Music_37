package lsh.framgia.com.isoundcloud.screen.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseActivity;
import lsh.framgia.com.isoundcloud.constant.TrackState;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.home.HomeFragment;
import lsh.framgia.com.isoundcloud.screen.main.home.HomePresenter;
import lsh.framgia.com.isoundcloud.screen.main.library.LibraryFragment;
import lsh.framgia.com.isoundcloud.screen.main.library.LibraryPresenter;
import lsh.framgia.com.isoundcloud.screen.main.search.SearchFragment;
import lsh.framgia.com.isoundcloud.screen.main.search.SearchPresenter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;
import lsh.framgia.com.isoundcloud.service.OnMediaPlayerStatusListener;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View,
        OnMediaPlayerStatusListener, View.OnClickListener, OnToolbarChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TextView mTextToolbarTitle;
    private BottomNavigationView mBottomNavigation;
    private ConstraintLayout mMiniPlayer;
    private TextView mTrackTitle;
    private TextView mTrackArtist;
    private ImageView mImageArtwork;
    private ImageView mImagePrevious;
    private ImageView mImagePlayPause;
    private ImageView mImageNext;
    private ProgressBar mProgressBarLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initLayout() {
        setPresenter(new MainPresenter());
        setupReferences();
        setupToolbar();
        setupListeners();
        replaceHomeFragment();
    }

    @Override
    protected OnMediaPlayerStatusListener getMediaPlayerStatusListener() {
        return this;
    }

    @Override
    protected void onMusicServiceConnected() {
        setupMiniPlayer(mMusicService.getCurrentTrack());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMusicService == null) return;
        mMusicService.setOnMediaPlayerStatusListener(this);
        setupMiniPlayer(mMusicService.getCurrentTrack());
    }

    @Override
    public void onTrackPrepared(Track track) {
        setupMiniPlayer(track);
    }

    @Override
    public void onTrackPaused() {
        mImagePlayPause.setImageResource(R.drawable.ic_play_white);
    }

    @Override
    public void onTrackResumed() {
        mImagePlayPause.setImageResource(R.drawable.ic_pause_white);
    }

    @Override
    public void onNewTrackRequested(Track track) {
        mImagePlayPause.setImageResource(R.drawable.ic_pause_white);
        setupMiniPlayer(track);
    }

    @Override
    public void onTrackError() {
        Toast.makeText(this, getString(R.string.error_player), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoopModeChanged(int loopMode) {

    }

    @Override
    public void onShuffleModeChanged(int shuffleMode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_mini_player:
                startActivity(new Intent(this, PlayerActivity.class));
                break;
            case R.id.image_mini_player_action:
                handlePlayerActionChanged();
                break;
            case R.id.image_mini_player_previous:
                mMusicService.playPreviousTrack();
                break;
            case R.id.image_mini_player_next:
                mMusicService.playNextTrack();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                replaceHomeFragment();
                return true;
            case R.id.action_search:
                replaceSearchFragment();
                return true;
            case R.id.action_library:
                replaceLibraryFragment();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onTitleChange(String title) {
        setToolbarTitle(title);
    }

    public void setPlaylist(List<Track> tracks) {
        mMusicService.setPlaylist(tracks);
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().show();
        mTextToolbarTitle.setText(title);
    }

    public void showActionAndBottomBar(boolean isShown) {
        if (isShown) {
            mBottomNavigation.setVisibility(View.VISIBLE);
            if (getSupportActionBar() != null) getSupportActionBar().show();
        } else {
            mBottomNavigation.setVisibility(View.GONE);
            if (getSupportActionBar() != null) getSupportActionBar().hide();
        }
    }

    private void replaceHomeFragment() {
        HomeFragment homeFragment = HomeFragment.newInstance();
        HomePresenter homePresenter = new HomePresenter();
        homePresenter.setView(homeFragment);
        replaceFragment(R.id.frame_container, homeFragment, false, null);
        setToolbarTitle(getString(R.string.label_home));
    }

    private void replaceSearchFragment() {
        getSupportActionBar().hide();
        SearchFragment searchFragment = SearchFragment.newInstance();
        SearchPresenter searchPresenter = new SearchPresenter(TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(), TrackLocalDataSource.getInstance(this)
        ));
        searchPresenter.setView(searchFragment);
        replaceFragment(R.id.frame_container, searchFragment, false, null);
    }

    private void replaceLibraryFragment() {
        LibraryFragment libraryFragment = LibraryFragment.newInstance();
        LibraryPresenter libraryPresenter = new LibraryPresenter();
        libraryPresenter.setView(libraryFragment);
        replaceFragment(R.id.frame_container, libraryFragment, false, null);
        setToolbarTitle(getString(R.string.label_library));
    }

    private void handlePlayerActionChanged() {
        if (mMusicService.isPlaying()) {
            mMusicService.stopPlayingMusic();
        } else {
            mMusicService.resumePlayingMusic();
        }
    }

    private void setupMiniPlayer(Track track) {
        if (track == null) return;
        mMiniPlayer.setVisibility(View.VISIBLE);
        mTrackTitle.setSelected(true);
        mTrackArtist.setSelected(true);
        displayArtworkImage(track);
        displayTrackInfo(track);
        updatePlayPauseView(mMusicService.getTrackState());
    }

    private void displayTrackInfo(Track track) {
        mTrackTitle.setText(track.getTitle());
        mTrackArtist.setText(track.getArtist());
    }

    private void updatePlayPauseView(int state) {
        if (state != TrackState.PREPARED && state != TrackState.PAUSED) {
            mProgressBarLoading.setVisibility(View.VISIBLE);
            mImagePlayPause.setVisibility(View.INVISIBLE);
        } else {
            mProgressBarLoading.setVisibility(View.GONE);
            mImagePlayPause.setVisibility(View.VISIBLE);
        }

        if (state == TrackState.PAUSED) onTrackPaused();
        else onTrackResumed();
    }

    private void displayArtworkImage(Track track) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.bg_track_place_holder)
                .placeholder(R.drawable.bg_track_place_holder);
        Glide.with(getApplicationContext())
                .load(track.getArtworkUrl())
                .apply(options)
                .into(mImageArtwork);
    }

    private void setupListeners() {
        mMiniPlayer.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImagePlayPause.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void setupReferences() {
        mToolbar = findViewById(R.id.toolbar_genre);
        mTextToolbarTitle = findViewById(R.id.text_toolbar_genre);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mMiniPlayer = findViewById(R.id.layout_mini_player);
        mTrackTitle = mMiniPlayer.findViewById(R.id.text_mini_player_title);
        mTrackArtist = mMiniPlayer.findViewById(R.id.text_mini_player_artist);
        mImageArtwork = mMiniPlayer.findViewById(R.id.image_mini_player_artwork);
        mImagePrevious = mMiniPlayer.findViewById(R.id.image_mini_player_previous);
        mImagePlayPause = mMiniPlayer.findViewById(R.id.image_mini_player_action);
        mImageNext = mMiniPlayer.findViewById(R.id.image_mini_player_next);
        mProgressBarLoading = mMiniPlayer.findViewById(R.id.progress_bar_loading);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
