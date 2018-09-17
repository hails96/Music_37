package lsh.framgia.com.isoundcloud.screen.main.home;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;

public interface HomeContract {
    interface View extends IView<Presenter> {

    }

    interface Presenter extends IPresenter<View> {

    }
}
