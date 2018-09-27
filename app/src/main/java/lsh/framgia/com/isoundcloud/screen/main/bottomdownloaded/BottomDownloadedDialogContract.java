package lsh.framgia.com.isoundcloud.screen.main.bottomdownloaded;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class BottomDownloadedDialogContract {
    interface View extends IView<Presenter> {
        void showTrack(Track track);
    }

    interface Presenter extends IPresenter<View> {

    }
}
