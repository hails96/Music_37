package lsh.framgia.com.isoundcloud.screen.main.playlist;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Playlist;

public interface PlaylistContract {
    interface View extends IView<Presenter> {
        void createPlaylistSuccess();

        void failedToCreatePlaylist(String msg);

        void showPlaylists(List<Playlist> playlists);
    }

    interface Presenter extends IPresenter<View> {
        void createNewPlaylist(String playlistName);

        void getPlaylists();
    }
}
