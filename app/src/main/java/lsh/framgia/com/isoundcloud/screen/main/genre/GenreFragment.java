package lsh.framgia.com.isoundcloud.screen.main.genre;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;

public class GenreFragment extends BaseFragment<GenreContract.Presenter>
        implements GenreContract.View {

    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_genre;
    }

    @Override
    protected void initLayout() {

    }
}
