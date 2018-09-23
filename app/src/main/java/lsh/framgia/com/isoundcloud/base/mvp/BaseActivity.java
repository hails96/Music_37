package lsh.framgia.com.isoundcloud.base.mvp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import lsh.framgia.com.isoundcloud.service.MusicService;
import lsh.framgia.com.isoundcloud.service.OnMediaPlayerStatusListener;
import lsh.framgia.com.isoundcloud.util.DialogUtils;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView<P> {

    protected P mPresenter;
    protected MusicService mMusicService;
    protected ServiceConnection mServiceConnection;
    protected boolean mIsBound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        prepareServiceConnection();
        initLayout();
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showProgress() {
        DialogUtils.showProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        DialogUtils.dismissProgressDialog();
    }

    @Override
    public void back() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) finish();
        else getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mIsBound = false;
    }

    public void replaceFragment(int containerId, Fragment fragment,
                                boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) transaction.addToBackStack(tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    private void prepareServiceConnection() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMusicService = ((MusicService.MusicBinder) service).getService();
                mIsBound = true;
                mMusicService.setOnMediaPlayerStatusListener(getMediaPlayerStatusListener());
                onMusicServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    protected abstract int getLayoutId();

    protected abstract void initLayout();

    protected OnMediaPlayerStatusListener getMediaPlayerStatusListener() {
        return null;
    }

    protected void onMusicServiceConnected() {
    }
}
