package lsh.framgia.com.isoundcloud.screen.main.genre;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.Genre;

public class GenrePresenter extends BasePresenter<GenreContract.View>
        implements GenreContract.Presenter {

    private Genre mGenre;

    @Override
    public void start() {
        mView.setupGenreView(mGenre);
    }

    public GenrePresenter setGenre(Genre genre) {
        mGenre = genre;
        return this;
    }
}
