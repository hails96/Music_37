package lsh.framgia.com.isoundcloud.base.mvp;

import android.content.Context;

public interface IView<P> {

    void setPresenter(P presenter);

    Context getViewContext();

    void showProgress();

    void hideProgress();
}
