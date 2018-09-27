package lsh.framgia.com.isoundcloud.screen.main.downloadedtrack;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface DownloadedTrackContract {
    interface View extends IView<Presenter> {
        void getDownloadedTracksSuccess(List<Track> tracks);
    }

    interface Presenter extends IPresenter<View> {
        void getDownloadedTracks();
    }
}
