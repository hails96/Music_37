package lsh.framgia.com.isoundcloud.screen.main.playlisttrack;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.constant.MenuType;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.OnPlaylistChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.OnToolbarChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogFragment;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogPresenter;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistTrackFragment extends BaseFragment<PlaylistTrackContract.Presenter>
        implements PlaylistTrackContract.View, TrackAdapter.OnTrackItemClickListener {

    private FloatingActionButton mFabPlay;
    private RecyclerView mRecyclerTrack;
    private TrackAdapter mTrackAdapter;
    private OnToolbarChangeListener mOnToolbarChangeListener;
    private OnPlaylistChangeListener mOnPlaylistChangeListener;
    private Playlist mPlaylist;

    public static PlaylistTrackFragment newInstance() {
        return new PlaylistTrackFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_downloaded_track;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnToolbarChangeListener = (OnToolbarChangeListener) context;
            mOnPlaylistChangeListener = (OnPlaylistChangeListener) context;
            mOnToolbarChangeListener.onTitleChange(mPlaylist.getName());
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnToolbarChangeListener/OnPlaylistChangeListener");
        }
    }

    @Override
    protected void initLayout() {
        setupRecyclerTrack();
        setupFabPlay();
        mPresenter.getTracksFromPlaylist(mPlaylist);
    }

    @Override
    public void getTracksSuccess(List<Track> result) {
        mTrackAdapter.addAll(result);
    }

    @Override
    public void onTrackClick(Track track) {
        startPlay(track);
    }

    @Override
    public void onMenuClick(Track track) {
        BottomMenuDialogFragment fragment = BottomMenuDialogFragment.newInstance();
        BottomMenuDialogPresenter presenter = new BottomMenuDialogPresenter(TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(), TrackLocalDataSource.getInstance(getContext())));
        presenter
                .setTrack(track)
                .setMenuType(MenuType.DOWNLOAD)
                .setView(fragment);
        fragment.show(getChildFragmentManager(), BottomMenuDialogFragment.class.getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnToolbarChangeListener == null) return;
        mOnToolbarChangeListener.onTitleChange(getString(R.string.text_playlist));
    }

    public PlaylistTrackFragment setPlaylist(Playlist playlist) {
        mPlaylist = playlist;
        return this;
    }

    private void setupFabPlay() {
        mFabPlay = mRootView.findViewById(R.id.fab_play);
        mFabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlay(mTrackAdapter.getTracks().get(0));
            }
        });
    }

    private void startPlay(Track track) {
        if (getActivity() == null) return;
        mOnPlaylistChangeListener.onPlaylistChange(mTrackAdapter.getTracks());
        mPresenter.addNumberOfPlays(mPlaylist);
        getActivity().startActivity(PlayerActivity.getPlayerIntent(getContext(), track));
    }

    private void setupRecyclerTrack() {
        mRecyclerTrack = mRootView.findViewById(R.id.recycler_downloaded_track);
        mRecyclerTrack.setHasFixedSize(true);
        mRecyclerTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrackAdapter = new TrackAdapter(getContext(), new ArrayList<Track>());
        mTrackAdapter.setOnTrackItemClickListener(this);
        mRecyclerTrack.setAdapter(mTrackAdapter);
    }
}
