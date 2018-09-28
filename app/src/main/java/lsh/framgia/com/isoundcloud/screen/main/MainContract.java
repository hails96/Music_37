package lsh.framgia.com.isoundcloud.screen.main;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;

public interface MainContract {
    interface View extends IView<Presenter> {
        void onUpdateDownloadedTrackFailure(String msg);
    }

    interface Presenter extends IPresenter<View> {
        void updateDownloadedTrack(long requestId);
    }
}
