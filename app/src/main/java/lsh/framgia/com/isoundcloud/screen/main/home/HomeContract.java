package lsh.framgia.com.isoundcloud.screen.main.home;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.Genre;

public interface HomeContract {
    interface View extends IView<Presenter> {

    }

    interface Presenter extends IPresenter<View> {
        List<Genre> getGenres();
    }
}
