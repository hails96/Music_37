package lsh.framgia.com.isoundcloud.screen.player.nowplaying;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;
import lsh.framgia.com.isoundcloud.util.DialogUtils;

public class NowPlayingFragment extends BottomSheetDialogFragment
        implements NowPlayingContract.View, TrackAdapter.OnTrackItemClickListener {

    private NowPlayingContract.Presenter mPresenter;

    private RecyclerView mRecyclerNowPlaying;
    private TrackAdapter mTrackAdapter;

    public static NowPlayingFragment newInstance() {
        return new NowPlayingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerNowPlaying(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(NowPlayingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showProgress() {
        DialogUtils.showProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        DialogUtils.dismissProgressDialog();
    }

    @Override
    public void back() {

    }

    @Override
    public void showNowPlayingPlaylist(List<Track> playlist, Track currentTrack) {
        if (playlist == null || currentTrack == null) return;
        mTrackAdapter.setCurrentTrack(currentTrack);
        mTrackAdapter.addAll(playlist);
    }

    @Override
    public void onTrackClick(Track track) {

    }

    @Override
    public void onMenuClick(Track track) {

    }

    private void setupRecyclerNowPlaying(View view) {
        mRecyclerNowPlaying = view.findViewById(R.id.recycler_now_playing);
        mRecyclerNowPlaying.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrackAdapter = new TrackAdapter(getContext(), new ArrayList<Track>());
        mTrackAdapter.setOnTrackItemClickListener(this);
        mRecyclerNowPlaying.setAdapter(mTrackAdapter);
    }
}
