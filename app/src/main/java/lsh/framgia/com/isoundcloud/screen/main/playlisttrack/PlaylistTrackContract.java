package lsh.framgia.com.isoundcloud.screen.main.playlisttrack;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface PlaylistTrackContract {
    interface View extends IView<Presenter> {
        void getTracksSuccess(List<Track> result);
    }

    interface Presenter extends IPresenter<View> {
        void getTracksFromPlaylist(Playlist playlist);

        void addNumberOfPlays(Playlist playlist);
    }
}
