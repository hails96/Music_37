package lsh.framgia.com.isoundcloud.screen.main.favorite;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class FavoritePresenter extends BasePresenter<FavoriteContract.View>
        implements FavoriteContract.Presenter {

    private TrackRepository mTrackRepository;

    public FavoritePresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void getFavoriteTracks() {
        mTrackRepository.getFavoriteTracks(new TrackDataSource.OnLocalResponseListener<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                mView.getFavoriteTracksSuccess(result);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
