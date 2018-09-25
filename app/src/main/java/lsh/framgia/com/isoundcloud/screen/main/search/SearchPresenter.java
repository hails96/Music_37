package lsh.framgia.com.isoundcloud.screen.main.search;

import android.support.annotation.Nullable;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask;
import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;

public class SearchPresenter extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter, BaseFetchingAsyncTask.OnResponseListener<List<Track>> {

    private static final int LIMIT_PER_PAGE = 20;

    private TrackRepository mTrackRepository;
    private int mOffset;

    public SearchPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void getSearchResult(String query) {
        if (query == null || query.trim().isEmpty()) return;
        mTrackRepository.getSearchTracks(query, mOffset, LIMIT_PER_PAGE, this);
    }

    @Override
    public void clearSearchQuery() {
        mOffset = 0;
    }

    @Override
    public void onSuccess(@Nullable List<Track> result) {
        mView.updateTracks(result);
        if (result == null) return;
        mOffset += result.size();
    }

    @Override
    public void onError(Exception e) {
        mView.showError(e.getMessage());
    }
}
