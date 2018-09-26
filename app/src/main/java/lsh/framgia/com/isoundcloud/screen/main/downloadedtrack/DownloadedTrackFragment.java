package lsh.framgia.com.isoundcloud.screen.main.downloadedtrack;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.OnToolbarChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedTrackFragment extends BaseFragment<DownloadedTrackContract.Presenter>
        implements DownloadedTrackContract.View, TrackAdapter.OnTrackItemClickListener {

    private RecyclerView mRecyclerDownloaded;
    private TrackAdapter mTrackAdapter;
    private OnToolbarChangeListener mOnToolbarChangeListener;

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
            mOnToolbarChangeListener.onTitleChange(getString(R.string.text_downloaded_tracks));
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnToolbarChangeListener");
        }
    }

    @Override
    protected void initLayout() {
        setupRecyclerDownloaded();
    }

    @Override
    public void onTrackClick(Track track) {

    }

    @Override
    public void onMenuClick(Track track) {

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
