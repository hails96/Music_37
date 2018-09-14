package lsh.framgia.com.isoundcloud.base.mvp;

public interface IPresenter<V> {

    void setView(V view);

    void start();
}
