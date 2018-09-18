package lsh.framgia.com.isoundcloud.screen.main.genre;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;

import lsh.framgia.com.isoundcloud.data.Genre;

public class GenreContract {
    interface View extends IView<Presenter> {
        void setupGenreView(Genre genre);
    }

    interface Presenter extends IPresenter<View> {

    }
}
