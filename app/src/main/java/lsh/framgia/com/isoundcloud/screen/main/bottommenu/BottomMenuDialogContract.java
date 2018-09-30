package lsh.framgia.com.isoundcloud.screen.main.bottommenu;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class BottomMenuDialogContract {
    interface View extends IView<Presenter> {
        void showTrack(Track track);

        void updateFavoriteSuccess(Boolean isFavorite);

        void enableDownloadAction();

        void enableDeleteAction();

        void deleteTrackSuccess(Track track);

        void deleteTrackFailed(String msg);

        void getPlaylistsSuccess(List<Playlist> playlists);
    }

    interface Presenter extends IPresenter<View> {
        void updateFavorite(Track track);

        boolean checkDownloadedTrack(Track track);

        void saveTrack(Track track);

        void deleteTrackFromDatabase(Track track);

        void addTrackToNewPlaylist(Track track, String playlistName);
    }
}
