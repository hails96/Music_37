package lsh.framgia.com.isoundcloud.screen.main.playlisttrack;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class PlaylistTrackPresenter extends BasePresenter<PlaylistTrackContract.View>
        implements PlaylistTrackContract.Presenter {

    private TrackRepository mTrackRepository;

    public PlaylistTrackPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void getTracksFromPlaylist(Playlist playlist) {
        mTrackRepository.getTracksFromPlaylist(playlist, new TrackDataSource.OnLocalResponseListener<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                if (result == null || result.isEmpty()) return;
                mView.getTracksSuccess(result);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void addNumberOfPlays(Playlist playlist) {
        mTrackRepository.addNumberOfPlays(playlist);
    }
}
