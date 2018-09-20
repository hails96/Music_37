package lsh.framgia.com.isoundcloud.screen.main.genre;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;

import lsh.framgia.com.isoundcloud.data.model.Genre;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class GenreContract {
    interface View extends IView<Presenter> {
        void setupGenreView(Genre genre);

        void setupTracks(List<Track> tracks);

        void showError(String message);
    }

    interface Presenter extends IPresenter<View> {

    }
}
