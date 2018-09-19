package lsh.framgia.com.isoundcloud.screen.main.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.BasePresenter;
import lsh.framgia.com.isoundcloud.data.model.Genre;

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    @Override
    public void start() {

    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(Arrays.asList(Genre.values()));
    }
}
