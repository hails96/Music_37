package lsh.framgia.com.isoundcloud.screen.main.bottommenu;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class BottomMenuDialogContract {
    interface View extends IView<Presenter> {
        void setupTrack(Track track);
    }

    interface Presenter extends IPresenter<View> {

    }
}
