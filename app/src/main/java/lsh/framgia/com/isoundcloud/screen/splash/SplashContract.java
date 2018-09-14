package lsh.framgia.com.isoundcloud.screen.splash;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;

public class SplashContract {
    interface View extends IView<Presenter> {

    }

    interface Presenter extends IPresenter<View> {

    }
}
