package lsh.framgia.com.isoundcloud.screen.main.favorite;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.OnPlaylistChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.OnToolbarChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.bottomdownloaded.BottomDownloadedDialogFragment;
import lsh.framgia.com.isoundcloud.screen.main.bottomdownloaded.BottomDownloadedDialogPresenter;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends BaseFragment<FavoriteContract.Presenter>
        implements FavoriteContract.View, TrackAdapter.OnTrackItemClickListener {

    private RecyclerView mRecyclerDownloaded;
    private TrackAdapter mTrackAdapter;
    private OnToolbarChangeListener mOnToolbarChangeListener;
    private OnPlaylistChangeListener mOnPlaylistChangeListener;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
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
            mOnToolbarChangeListener.onTitleChange(getString(R.string.text_favorite_tracks));
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnToolbarChangeListener/OnPlaylistChangeListener");
        }
    }

    @Override
    protected void initLayout() {
        setupRecyclerDownloaded();
        mPresenter.getFavoriteTracks();
    }

    @Override
    public void getFavoriteTracksSuccess(List<Track> tracks) {
        if (tracks == null) return;
        mTrackAdapter.addAll(tracks);
    }

    @Override
    public void onTrackClick(Track track) {
        if (getActivity() == null) return;
        mOnPlaylistChangeListener.onPlaylistChange(mTrackAdapter.getTracks());
        getActivity().startActivity(PlayerActivity.getPlayerIntent(getContext(), track));
    }

    @Override
    public void onMenuClick(Track track) {
        BottomDownloadedDialogFragment fragment = BottomDownloadedDialogFragment.newInstance();
        BottomDownloadedDialogPresenter presenter = new BottomDownloadedDialogPresenter();
        presenter
                .setTrack(track)
                .setView(fragment);
        fragment.show(getChildFragmentManager(), BottomDownloadedDialogFragment.class.getSimpleName());
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
