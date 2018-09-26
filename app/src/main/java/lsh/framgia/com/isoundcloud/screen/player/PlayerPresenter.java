package lsh.framgia.com.isoundcloud.screen.player;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource.OnLocalResponseListener;

public class PlayerPresenter extends BasePresenter<PlayerContract.View>
        implements PlayerContract.Presenter {

    private TrackRepository mTrackRepository;

    public PlayerPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void updateDownloadedTrack(long requestId) {
        mTrackRepository.updateDownloadedTrack(
                requestId,
                new OnLocalResponseListener() {
                    @Override
                    public void onSuccess(Object result) {

                    }

                    @Override
                    public void onFailure(String msg) {
                        mView.onUpdateDownloadedTrackFailure(msg);
                    }
                });
    }

    @Override
    public void saveTrack(Track track) {
        mTrackRepository.saveTrackToDatabase(track);
    }

    @Override
    public boolean checkDownloadedTrack(Track track) {
        return mTrackRepository.isDownloadedTrack(track);
    }

    @Override
    public void updateFavorite(Track track) {
        mTrackRepository.updateFavoriteTrack(track, new OnLocalResponseListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isFavorite) {
                mView.updateFavoriteSuccess(isFavorite);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public boolean isFavoriteTrack(Track track) {
        return mTrackRepository.isFavoriteTrack(track);
    }
}
