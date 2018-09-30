package lsh.framgia.com.isoundcloud.screen.main.library;


import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.downloadedtrack.DownloadedTrackFragment;
import lsh.framgia.com.isoundcloud.screen.main.downloadedtrack.DownloadedTrackPresenter;
import lsh.framgia.com.isoundcloud.screen.main.favorite.FavoriteFragment;
import lsh.framgia.com.isoundcloud.screen.main.favorite.FavoritePresenter;
import lsh.framgia.com.isoundcloud.screen.main.playlist.PlaylistFragment;
import lsh.framgia.com.isoundcloud.screen.main.playlist.PlaylistPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends BaseFragment<LibraryContract.Presenter>
        implements LibraryContract.View, View.OnClickListener {

    private CardView mViewDownloaded;
    private CardView mViewFavorite;
    private CardView mViewPlaylist;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_library;
    }

    @Override
    protected void initLayout() {
        setupPreferences();
        setupListeners();
    }

    private void setupPreferences() {
        mViewDownloaded = mRootView.findViewById(R.id.card_view_downloaded);
        mViewFavorite = mRootView.findViewById(R.id.card_view_favorite);
        mViewPlaylist = mRootView.findViewById(R.id.card_view_playlist);
    }

    private void setupListeners() {
        mViewDownloaded.setOnClickListener(this);
        mViewFavorite.setOnClickListener(this);
        mViewPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view_downloaded:
                goToDownloadedTracksScreen();
                break;
            case R.id.card_view_favorite:
                goToFavoriteScreen();
                break;
            case R.id.card_view_playlist:
                goToPlaylistScreen();
                break;
            default:
                break;
        }
    }

    private void goToPlaylistScreen() {
        PlaylistFragment fragment = PlaylistFragment.newInstance();
        PlaylistPresenter presenter = new PlaylistPresenter(
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance(getContext())));
        presenter.setView(fragment);
        replaceFragment(R.id.frame_container, fragment, true, null);
    }

    private void goToFavoriteScreen() {
        FavoriteFragment fragment = FavoriteFragment.newInstance();
        FavoritePresenter presenter = new FavoritePresenter(
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance(getContext())));
        presenter.setView(fragment);
        replaceFragment(R.id.frame_container, fragment, true, null);
    }

    private void goToDownloadedTracksScreen() {
        DownloadedTrackFragment fragment = DownloadedTrackFragment.newInstance();
        DownloadedTrackPresenter presenter = new DownloadedTrackPresenter(
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance(getContext())));
        presenter.setView(fragment);
        replaceFragment(R.id.frame_container, fragment, true, null);
    }
}
