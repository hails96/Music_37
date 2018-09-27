package lsh.framgia.com.isoundcloud.screen.player.nowplaying;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class NowPlayingContract {
    interface View extends IView<Presenter> {
        void showNowPlayingPlaylist(List<Track> playlist, Track currentTrack);
    }

    interface Presenter extends IPresenter<View> {

    }
}
