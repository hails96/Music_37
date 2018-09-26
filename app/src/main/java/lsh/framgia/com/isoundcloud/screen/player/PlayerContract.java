package lsh.framgia.com.isoundcloud.screen.player;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class PlayerContract {
    interface View extends IView<Presenter> {

    }

    interface Presenter extends IPresenter<View> {
        void updateDownloadedTrack(long requestId);

        void saveTrack(Track track);

        boolean checkDownloadedTrack(Track track);
    }
}
