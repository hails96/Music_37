package lsh.framgia.com.isoundcloud.screen.main;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseActivity;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.genre.GenreFragment;
import lsh.framgia.com.isoundcloud.screen.main.home.HomeFragment;
import lsh.framgia.com.isoundcloud.screen.main.home.HomePresenter;
import lsh.framgia.com.isoundcloud.screen.main.search.SearchFragment;
import lsh.framgia.com.isoundcloud.screen.main.search.SearchPresenter;
import lsh.framgia.com.isoundcloud.service.OnMediaPlayerStatusListener;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View,
        FragmentManager.OnBackStackChangedListener, OnMediaPlayerStatusListener {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigation;

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
        HomeFragment homeFragment = HomeFragment.newInstance();
        HomePresenter homePresenter = new HomePresenter();
        homePresenter.setView(homeFragment);
        replaceFragment(R.id.frame_container, homeFragment, false, null);
    }

    @Override
    protected OnMediaPlayerStatusListener getMediaPlayerStatusListener() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                goToSearchScreen();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(GenreFragment.class.getSimpleName());
        if (fragment instanceof GenreFragment && fragment.isVisible()) {
            mBottomNavigation.setVisibility(View.GONE);
            if (getSupportActionBar() != null) getSupportActionBar().hide();
        } else {
            mBottomNavigation.setVisibility(View.VISIBLE);
            if (getSupportActionBar() != null) getSupportActionBar().show();
        }
    }

    @Override
    protected void onMusicServiceConnected() {
        if (mMusicService.isPlaying()) {
            // TODO: Bind track info to mini player
        }
    }

    @Override
    public void onTrackPrepared(Track track) {

    }

    @Override
    public void onTrackPaused() {

    }

    @Override
    public void onTrackResumed() {

    }

    @Override
    public void onNewTrackRequested(Track track) {

    }

    @Override
    public void onTrackError() {
        Toast.makeText(this, getString(R.string.error_player), Toast.LENGTH_SHORT).show();
    }

    public void setPlaylist(List<Track> tracks) {
        mMusicService.setPlaylist(tracks);
    }

    private void goToSearchScreen() {
        SearchFragment searchFragment = SearchFragment.newInstance();
        SearchPresenter searchPresenter = new SearchPresenter();
        searchPresenter.setView(searchFragment);
        replaceFragment(R.id.frame_container, searchFragment, true, null);
    }

    private void setupListeners() {
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    private void setupReferences() {
        mToolbar = findViewById(R.id.toolbar_genre);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
