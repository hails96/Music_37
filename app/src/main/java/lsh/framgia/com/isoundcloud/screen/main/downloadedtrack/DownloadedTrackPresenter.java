package lsh.framgia.com.isoundcloud.screen.main.downloadedtrack;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class DownloadedTrackPresenter extends BasePresenter<DownloadedTrackContract.View>
        implements DownloadedTrackContract.Presenter {

    private TrackRepository mTrackRepository;

    public DownloadedTrackPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void getDownloadedTracks() {
        mTrackRepository.getDownloadedTracks(new TrackDataSource.OnLocalResponseListener<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                mView.getDownloadedTracksSuccess(result);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
