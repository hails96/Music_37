package lsh.framgia.com.isoundcloud.screen.main.playlist;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class PlaylistPresenter extends BasePresenter<PlaylistContract.View>
        implements PlaylistContract.Presenter {

    private TrackRepository mTrackRepository;

    public PlaylistPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void start() {

    }

    @Override
    public void createNewPlaylist(String playlistName) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setCreatedDate(System.currentTimeMillis());
        mTrackRepository.createNewPlaylist(playlist, new TrackDataSource.OnLocalResponseListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mView.createPlaylistSuccess();
            }

            @Override
            public void onFailure(String msg) {
                mView.failedToCreatePlaylist(msg);
            }
        });
    }

    @Override
    public void getPlaylists() {
        mTrackRepository.getPlaylists(new TrackDataSource.OnLocalResponseListener<List<Playlist>>() {
            @Override
            public void onSuccess(List<Playlist> result) {
                if (result == null || result.isEmpty()) return;
                mView.showPlaylists(result);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
