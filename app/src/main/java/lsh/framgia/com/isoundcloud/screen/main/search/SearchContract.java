package lsh.framgia.com.isoundcloud.screen.main.search;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class SearchContract {
    interface View extends IView<Presenter> {
        void showError(String message);

        void updateTracks(List<Track> result);
    }

    interface Presenter extends IPresenter<View> {
        void getSearchResult(String query);

        void clearSearchQuery();
    }
}
