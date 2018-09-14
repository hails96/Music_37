package lsh.framgia.com.isoundcloud.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView<P> {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initLayout();
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    protected abstract int getLayoutId();

    protected abstract void initLayout();
}
