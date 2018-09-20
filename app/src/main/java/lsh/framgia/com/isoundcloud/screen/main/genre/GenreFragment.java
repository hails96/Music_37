package lsh.framgia.com.isoundcloud.screen.main.genre;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.model.Genre;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class GenreFragment extends BaseFragment<GenreContract.Presenter>
        implements GenreContract.View, TrackAdapter.OnTrackItemClickListener {

    private Toolbar mToolbar;
    private ImageView mImageArtwork;
    private TextView mTextToolbarGenre;
    private RecyclerView mRecyclerTrack;

    private TrackAdapter mTrackAdapter;

    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_genre;
    }

    @Override
    protected void initLayout() {
        setupPreferences();
        setupRecyclerGenre();
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
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerGenre() {
        mRecyclerTrack.setHasFixedSize(true);
        mRecyclerTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerTrack.setNestedScrollingEnabled(false);
        mRecyclerTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrackAdapter = new TrackAdapter(getContext(), new ArrayList<Track>(), this);
        mRecyclerTrack.setAdapter(mTrackAdapter);
    }

    private void setupPreferences() {
        mToolbar = mRootView.findViewById(R.id.toolbar_genre);
        mImageArtwork = mRootView.findViewById(R.id.image_artwork);
        mTextToolbarGenre = mRootView.findViewById(R.id.text_toolbar_genre);
        mRecyclerTrack = mRootView.findViewById(R.id.recycler_song);
    }

    @Override
    public void onTrackClick(Track track) {
        Toast.makeText(getContext(), track.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuClick(Track track) {
        Toast.makeText(getContext(), track.getArtist(), Toast.LENGTH_SHORT).show();
    }
}
