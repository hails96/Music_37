package lsh.framgia.com.isoundcloud.base.mvp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView<P> {

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getLayoutId());
        initLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
    }

    protected abstract int getLayoutId();

    protected abstract void initLayout();
}
