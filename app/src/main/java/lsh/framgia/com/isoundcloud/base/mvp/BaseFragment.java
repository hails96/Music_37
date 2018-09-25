package lsh.framgia.com.isoundcloud.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import lsh.framgia.com.isoundcloud.util.DialogUtils;

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
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showProgress() {
        DialogUtils.showProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        DialogUtils.dismissProgressDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void back() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null) return;
        FragmentManager fragmentManager = baseActivity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            baseActivity.finish();
        } else {
            fragmentManager.popBackStack();
        }
    }

    protected void showKeyBoard(EditText editText) {
        if (getActivity() == null) return;
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    protected void hideKeyBoard(View view) {
        if (getActivity() != null && view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected void replaceFragment(int containerId, Fragment fragment,
                                   boolean addToBackStack, String tag) {
        if (getActivity() == null) return;
        ((BaseActivity) getActivity()).replaceFragment(containerId, fragment, addToBackStack, tag);
    }

    protected abstract int getLayoutId();

    protected abstract void initLayout();
}
