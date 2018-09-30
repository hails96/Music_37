package lsh.framgia.com.isoundcloud.screen.main.downloadedtrack;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.constant.MenuType;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.OnPlaylistChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.OnToolbarChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogFragment;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogPresenter;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.OnDeleteListener;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedTrackFragment extends BaseFragment<DownloadedTrackContract.Presenter>
        implements DownloadedTrackContract.View, TrackAdapter.OnTrackItemClickListener, OnDeleteListener {

    private RecyclerView mRecyclerDownloaded;
    private TrackAdapter mTrackAdapter;
    private OnToolbarChangeListener mOnToolbarChangeListener;
    private OnPlaylistChangeListener mOnPlaylistChangeListener;

    public static DownloadedTrackFragment newInstance() {
        return new DownloadedTrackFragment();
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
            mOnToolbarChangeListener.onTitleChange(getString(R.string.text_downloaded_tracks));
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnToolbarChangeListener/OnPlaylistChangeListener");
        }
    }

    @Override
    protected void initLayout() {
        setupRecyclerDownloaded();
        mPresenter.getDownloadedTracks();
    }

    @Override
    public void onTrackClick(Track track) {
        if (getActivity() == null) return;
        mOnPlaylistChangeListener.onPlaylistChange(mTrackAdapter.getTracks());
        getActivity().startActivity(PlayerActivity.getPlayerIntent(getContext(), track));
    }

    @Override
    public void onMenuClick(Track track) {
        BottomMenuDialogFragment fragment = BottomMenuDialogFragment.newInstance();
        BottomMenuDialogPresenter presenter = new BottomMenuDialogPresenter(TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(), TrackLocalDataSource.getInstance(getContext())));
        presenter
                .setTrack(track)
                .setMenuType(MenuType.DELETE)
                .setOnDeleteListener(this)
                .setView(fragment);
        fragment.show(getChildFragmentManager(), BottomMenuDialogFragment.class.getSimpleName());
    }

    @Override
    public void getDownloadedTracksSuccess(List<Track> tracks) {
        mTrackAdapter.getTracks().clear();
        mTrackAdapter.addAll(tracks);
        mTrackAdapter.notifyDataSetChanged();
        mOnPlaylistChangeListener.onPlaylistChange(mTrackAdapter.getTracks());
    }

    @Override
    public void onTrackDeleted() {
        mPresenter.getDownloadedTracks();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnToolbarChangeListener == null) return;
        mOnToolbarChangeListener.onTitleChange(getString(R.string.label_library));
    }

    private void setupRecyclerDownloaded() {
        mRecyclerDownloaded = mRootView.findViewById(R.id.recycler_downloaded_track);
        mRecyclerDownloaded.setHasFixedSize(true);
        mRecyclerDownloaded.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrackAdapter = new TrackAdapter(getContext(), new ArrayList<Track>());
        mTrackAdapter.setOnTrackItemClickListener(this);
        mRecyclerDownloaded.setAdapter(mTrackAdapter);
    }
}
