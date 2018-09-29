package lsh.framgia.com.isoundcloud.screen.main.genre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.EndlessScrollListener;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.constant.MenuType;
import lsh.framgia.com.isoundcloud.data.model.Genre;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.MainActivity;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogFragment;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogPresenter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

public class GenreFragment extends BaseFragment<GenreContract.Presenter> implements GenreContract.View,
        TrackAdapter.OnTrackItemClickListener, View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mImageArtwork;
    private TextView mTextToolbarGenre;
    private FloatingActionButton mFloatingButtonPlay;
    private RecyclerView mRecyclerTrack;
    private ProgressBar mProgressBarLoading;

    private TrackAdapter mTrackAdapter;

    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_genre;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgress();
        mPresenter.getTracks();
        if (getActivity() == null) return;
        ((MainActivity) getActivity()).showActionAndBottomBar(false);
    }

    @Override
    protected void initLayout() {
        setupPreferences();
        setupListeners();
        setupRecyclerTrack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() == null) return;
        ((MainActivity) getActivity()).showActionAndBottomBar(true);
    }

    @Override
    public void setupGenreView(Genre genre) {
        if (genre == null) return;
        mImageArtwork.setImageResource(genre.getArtworkResId());
        mTextToolbarGenre.setText(getString(genre.getNameResId()));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
    }

    @Override
    public void updateTracks(List<Track> tracks) {
        mTrackAdapter.addAll(tracks);
        mProgressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mProgressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void onTrackClick(Track track) {
        if (getActivity() == null) return;
        ((MainActivity) getActivity()).setPlaylist(mTrackAdapter.getTracks());
        getActivity().startActivity(PlayerActivity.getPlayerIntent(getContext(), track));
    }

    @Override
    public void onMenuClick(Track track) {
        BottomMenuDialogFragment fragment = BottomMenuDialogFragment.newInstance();
        BottomMenuDialogPresenter presenter = new BottomMenuDialogPresenter(
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance(getContext())));
        presenter
                .setTrack(track)
                .setMenuType(MenuType.DOWNLOAD)
                .setView(fragment);
        fragment.show(getChildFragmentManager(), BottomMenuDialogFragment.class.getSimpleName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button_play:
                playFirstTrack();
                break;
            default:
                break;
        }
    }

    private void playFirstTrack() {
        ((MainActivity) getActivity()).setPlaylist(mTrackAdapter.getTracks());
        getActivity().startActivity(PlayerActivity.getPlayerIntent(
                getContext(), mTrackAdapter.getTracks().get(0)));
    }

    private void setupListeners() {
        mFloatingButtonPlay.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void setupRecyclerTrack() {
        mRecyclerTrack.setHasFixedSize(true);
        mRecyclerTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrackAdapter = new TrackAdapter(getContext(), new ArrayList<Track>());
        mTrackAdapter.setOnTrackItemClickListener(this);
        mRecyclerTrack.setAdapter(mTrackAdapter);
        mRecyclerTrack.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                mProgressBarLoading.setVisibility(View.VISIBLE);
                mPresenter.getTracks();
            }
        });
    }

    private void setupPreferences() {
        mToolbar = mRootView.findViewById(R.id.toolbar_genre);
        mImageArtwork = mRootView.findViewById(R.id.image_artwork);
        mTextToolbarGenre = mRootView.findViewById(R.id.text_toolbar_genre);
        mFloatingButtonPlay = mRootView.findViewById(R.id.floating_button_play);
        mRecyclerTrack = mRootView.findViewById(R.id.recycler_track);
        mProgressBarLoading = mRootView.findViewById(R.id.progress_bar_loading);
    }
}
