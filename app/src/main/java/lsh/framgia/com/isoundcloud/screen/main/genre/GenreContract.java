package lsh.framgia.com.isoundcloud.screen.main.genre;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;

public class GenreContract {
    interface View extends IView<Presenter> {

    }

    interface Presenter extends IPresenter<View> {

    }
}
