package lsh.framgia.com.isoundcloud.screen.main;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseActivity;
import lsh.framgia.com.isoundcloud.screen.main.home.HomeFragment;
import lsh.framgia.com.isoundcloud.screen.main.home.HomePresenter;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {

    private Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initLayout() {
        setPresenter(new MainPresenter());
        setupReferences();
        setupToolbar();
        HomeFragment homeFragment = HomeFragment.newInstance();
        HomePresenter homePresenter = new HomePresenter();
        homePresenter.setView(homeFragment);
        replaceFragment(R.id.frame_container, homeFragment, false, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    private void setupReferences() {
        mToolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
