package lsh.framgia.com.isoundcloud.screen.main.home;


import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.model.Genre;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.MainActivity;
import lsh.framgia.com.isoundcloud.screen.main.genre.GenreFragment;
import lsh.framgia.com.isoundcloud.screen.main.genre.GenrePresenter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View,
        GenreAdapter.OnGenreClickListener, RecentlyDownloadedAdapter.OnTrackClickListener {

    private static final int GENRE_COLUMN_NUMBERS = 2;
    private static final int RECENTLY_DOWNLOAD_COLUMN_NUMBERS = 3;
    private static final int SPLIT_NUMBER = 3;
    private static final int DOUBLE_COLUMN = 2;
    private static final int SINGLE_COLUMN = 1;

    private RecyclerView mRecyclerRecentlyDownloaded;
    private RecyclerView mRecyclerGenre;
    private TextView mTextNoSong;
    private ImageView mImageNoSong;
    private List<Track> mTracks;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initLayout() {
        setupPreferences();
        setupRecyclerGenre();
        setupRecyclerRecentlyDownload();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getDownloadedTracks();
    }

    @Override
    public void showRecentlyDownloadTracks(List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) {
            mTextNoSong.setVisibility(View.VISIBLE);
            mImageNoSong.setVisibility(View.VISIBLE);
            mRecyclerRecentlyDownloaded.setVisibility(View.GONE);
        } else {
            mTracks = tracks;
            mTextNoSong.setVisibility(View.INVISIBLE);
            mImageNoSong.setVisibility(View.INVISIBLE);
            mRecyclerRecentlyDownloaded.setVisibility(View.VISIBLE);
            Collections.reverse(tracks);
            mRecyclerRecentlyDownloaded.setAdapter(
                    new RecentlyDownloadedAdapter(getContext(), tracks, this));
        }
    }

    @Override
    public void onGenreClick(Genre genre) {
        GenreFragment genreFragment = GenreFragment.newInstance();
        GenrePresenter genrePresenter = new GenrePresenter();
        genrePresenter
                .setGenre(genre)
                .setView(genreFragment);
        replaceFragment(R.id.frame_container, genreFragment, true, Genre.class.getSimpleName());
    }

    @Override
    public void onTrackClick(Track track) {
        ((MainActivity) getActivity()).setPlaylist(mTracks);
        getActivity().startActivity(PlayerActivity.getPlayerIntent(getContext(), track));
    }

    private void setupRecyclerRecentlyDownload() {
        mRecyclerRecentlyDownloaded.setHasFixedSize(true);
        mRecyclerRecentlyDownloaded.setLayoutManager(
                new GridLayoutManager(getContext(), RECENTLY_DOWNLOAD_COLUMN_NUMBERS));
    }

    private void setupRecyclerGenre() {
        mRecyclerGenre.setHasFixedSize(true);
        mRecyclerGenre.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GENRE_COLUMN_NUMBERS);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % SPLIT_NUMBER == 0) {
                    return DOUBLE_COLUMN;
                } else {
                    return SINGLE_COLUMN;
                }
            }
        });
        mRecyclerGenre.setLayoutManager(gridLayoutManager);
        mRecyclerGenre.setAdapter(new GenreAdapter(getContext(), mPresenter.getGenres(), this));
    }

    private void setupPreferences() {
        mRecyclerRecentlyDownloaded = mRootView.findViewById(R.id.recycler_recently_downloaded);
        mRecyclerGenre = mRootView.findViewById(R.id.recycler_genre);
        mTextNoSong = mRootView.findViewById(R.id.text_no_song_avaiable);
        mImageNoSong = mRootView.findViewById(R.id.image_no_song_available);
    }
}
