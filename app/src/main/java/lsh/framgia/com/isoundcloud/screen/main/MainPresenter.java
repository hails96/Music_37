package lsh.framgia.com.isoundcloud.screen.main;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class MainPresenter extends BasePresenter<MainContract.View>
        implements MainContract.Presenter {

    private TrackRepository mTrackRepository;

    public MainPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void updateDownloadedTrack(long requestId) {
        mTrackRepository.updateDownloadedTrack(
                requestId,
                new TrackDataSource.OnLocalResponseListener() {
                    @Override
                    public void onSuccess(Object result) {

                    }

                    @Override
                    public void onFailure(String msg) {
                        mView.onUpdateDownloadedTrackFailure(msg);
                    }
                });
    }
}
