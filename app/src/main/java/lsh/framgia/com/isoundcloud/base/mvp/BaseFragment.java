package lsh.framgia.com.isoundcloud.base.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView<P> {

    protected P mPresenter;
    protected View mRootView;

    public BaseFragment() {
        // Require empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initLayout();
        return mRootView;
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    protected void replaceFragment(int containerId, Fragment fragment,
                                   boolean addToBackStack, String tag) {
        if (getActivity() == null) return;
        ((BaseActivity) getActivity()).replaceFragment(containerId, fragment, addToBackStack, tag);
    }

    protected abstract int getLayoutId();

    protected abstract void initLayout();
}
