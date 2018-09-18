package lsh.framgia.com.isoundcloud.base.mvp;

public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    protected V mView;

    @Override
    public void setView(V view) {
        mView = view;
        mView.setPresenter(this);
    }

    public abstract void start();
}
