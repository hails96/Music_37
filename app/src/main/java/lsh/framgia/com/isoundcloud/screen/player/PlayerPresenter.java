package lsh.framgia.com.isoundcloud.screen.player;

import android.util.Log;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource.OnLocalResponseListener;

public class PlayerPresenter extends BasePresenter<PlayerContract.View>
        implements PlayerContract.Presenter {

    private TrackRepository mTrackRepository;
    private OnLocalResponseListener mOnLocalResponseListener = new OnLocalResponseListener() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(String msg) {
            Log.d("PlayerPresenter", "onFailure");
        }
    };

    public PlayerPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void updateDownloadedTrack(long requestId) {
        mTrackRepository.updateDownloadedTrack(requestId, mOnLocalResponseListener);
    }

    @Override
    public void saveTrack(Track track) {
        mTrackRepository.saveTrackToDatabase(track, mOnLocalResponseListener);
    }

    @Override
    public boolean checkDownloadedTrack(Track track) {
        return mTrackRepository.isTrackDownloaded(track);
    }
}
