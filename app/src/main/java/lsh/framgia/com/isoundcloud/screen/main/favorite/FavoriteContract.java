package lsh.framgia.com.isoundcloud.screen.main.favorite;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.mvp.IPresenter;
import lsh.framgia.com.isoundcloud.base.mvp.IView;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface FavoriteContract {
    interface View extends IView<Presenter> {
        void getFavoriteTracksSuccess(List<Track> tracks);
    }

    interface Presenter extends IPresenter<View> {
        void getFavoriteTracks();
    }
}
