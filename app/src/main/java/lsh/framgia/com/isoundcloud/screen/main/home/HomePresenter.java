package lsh.framgia.com.isoundcloud.screen.main.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Genre;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private TrackRepository mTrackRepository;

    public HomePresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(Arrays.asList(Genre.values()));
    }

    @Override
    public void getDownloadedTracks() {
        mTrackRepository.getDownloadedTracks(new TrackDataSource.OnLocalResponseListener<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                mView.showRecentlyDownloadTracks(result);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
