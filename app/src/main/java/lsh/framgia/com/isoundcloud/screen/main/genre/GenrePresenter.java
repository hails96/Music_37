package lsh.framgia.com.isoundcloud.screen.main.genre;

import android.support.annotation.Nullable;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask;
import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Genre;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;

public class GenrePresenter extends BasePresenter<GenreContract.View>
        implements GenreContract.Presenter, BaseFetchingAsyncTask.OnResponseListener<List<Track>> {

    private static final int LIMIT_PER_PAGE = 20;

    private TrackRepository mTrackRepository;
    private Genre mGenre;
    private int mOffset;

    @Override
    public void start() {
        mView.showProgress();
        mView.setupGenreView(mGenre);
        mTrackRepository = TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(), TrackLocalDataSource.getInstance());
        getTracks();
    }

    @Override
    public void getTracks() {
        mTrackRepository.getTracks(mGenre.getKey(), mOffset, LIMIT_PER_PAGE, this);
    }

    public GenrePresenter setGenre(Genre genre) {
        mGenre = genre;
        return this;
    }

    @Override
    public void onSuccess(@Nullable List<Track> result) {
        mView.hideProgress();
        mView.updateTracks(result);
        if (result == null) return;
        mOffset += result.size();
    }

    @Override
    public void onError(Exception e) {
        mView.showError(e.getMessage());
        mView.hideProgress();
    }
}
